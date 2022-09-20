package click.recraft.zombiehero.item.skill

import click.recraft.share.item
import click.recraft.zombiehero.EntityManager
import click.recraft.zombiehero.item.CustomItem
import org.bukkit.Material
import org.bukkit.entity.*
import java.util.*
import kotlin.collections.HashMap

abstract class SkillItem(
    material: Material
): CustomItem(
    item(
        material,
        localizedName = UUID.randomUUID().toString(),
    )
) {
    private data class Passenger(val player: Player, val armorStand: Entity, val entity2: Entity) {
        init {
            entity2.addPassenger(armorStand)
            player.addPassenger(entity2)
        }
        fun clear() {
            armorStand.remove()
            entity2.remove()
        }
    }
    private val save: HashMap<UUID, Passenger>  = hashMapOf()
    fun containsPassenger(player: Player): Boolean {
        return save.contains(player.uniqueId)
    }
    fun createPassenger(player: Player) {
        val world = player.world
        val loc = world.spawnLocation.clone()
        val entity = (world.spawnEntity(loc, EntityType.TURTLE) as Turtle).apply {
            setBaby()
            ageLock = true
            isSilent = true
            isInvulnerable = true
            isInvisible = true
        }
        val armorStand = (world.spawnEntity(loc, EntityType.ARMOR_STAND) as ArmorStand).apply {
            isSmall = true
            isSilent = true
            customName = "\uE000"
            isCustomNameVisible = true
            isInvisible = true
            isInvulnerable = true
        }
        save[player.uniqueId] = Passenger(player, armorStand, entity)
        EntityManager.register(entity)
        EntityManager.register(armorStand)
    }
    fun removePassenger(player: Player) {
        val data = save[player.uniqueId] ?: return
        data.clear()
        save.remove(player.uniqueId)
    }
}