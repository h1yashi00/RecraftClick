package click.recraft.zombiehero.item.grenade

import click.recraft.zombiehero.ZombieHero
import click.recraft.zombiehero.event.GrenadeExplodeEvent
import click.recraft.zombiehero.item.CustomItemListener
import org.bukkit.Bukkit
import org.bukkit.entity.Item
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityExplodeEvent
import org.bukkit.event.entity.EntityPickupItemEvent
import java.util.*
import kotlin.collections.HashMap

class GrenadeListener: CustomItemListener (
    ZombieHero.plugin.customItemFactory
) {
    private data class GrenadeDamageData(
        val entity: UUID,
        val grenade: Grenade,
        val damagedEntity: HashMap<UUID, Double> = hashMapOf(),
        val gameTime: Int = ZombieHero.plugin.getTime()
    )
    private val save: HashMap<UUID, GrenadeDamageData> = hashMapOf()
    @EventHandler
    fun blockDamage(event: EntityExplodeEvent) {
        val damager = event.entity
        if (damager !is Item) return
        val data = save[damager.uniqueId] ?: return
        save.remove(damager.uniqueId)
        val player = Bukkit.getPlayer(damager.owner!!) ?: return
        if (ZombieHero.plugin.getTime() != data.gameTime) return
        val customEvent = GrenadeExplodeEvent(
            player,
            data.grenade,
            data.damagedEntity,
            event.blockList()
        )
        Bukkit.getPluginManager().callEvent(customEvent)
        event.isCancelled = true
        if (customEvent.cancel) {
            return
        }
        data.damagedEntity.iterator().forEach { (uuid, damage) ->
            val entity = Bukkit.getEntity(uuid)
            if (entity !is LivingEntity) return@forEach
            entity.damage(damage)
        }
    }
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = false)
    fun damage3(event: EntityDamageByEntityEvent) {
        val damager = event.damager
        if (damager !is Item) return
        val customItem = getItem(damager.itemStack) ?: return
        if (customItem !is Grenade) return
        event.isCancelled = true
        val victim = event.entity
        val data = save[damager.uniqueId] ?: GrenadeDamageData(damager.uniqueId, customItem)
        data.damagedEntity[victim.uniqueId] = event.damage
        save[damager.uniqueId] = data

        event.damage = 0.0
    }
    @EventHandler
    fun pickUp(event: EntityPickupItemEvent) {
        val player = if (event.entity is Player) {event.entity as Player} else {return}
        val customItem = getItem(event.item.itemStack) ?: return
        val grenade = if (customItem is Grenade) {customItem}  else return
        event.isCancelled = true
        grenade.pickUp(player, event.item)
    }
}