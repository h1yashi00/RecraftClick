package click.recraft.zombiehero.monster.api

import click.recraft.zombiehero.Util
import click.recraft.zombiehero.event.MonsterAttackPlayerEvent
import org.bukkit.Bukkit
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.player.PlayerInteractEvent

abstract class MonsterListener(
    private val monsterManager: MonsterManager = MonsterManager
): Listener {
    fun get(entity: Entity): Monster? {
        val player = if (entity is Player) {entity} else return null
        return monsterManager.get(player)
    }
    @EventHandler(priority= EventPriority.HIGHEST, ignoreCancelled = true)
    fun damage(event: EntityDamageEvent) {
        val monster = get(event.entity) ?: return
        val loc = event.entity.location
        loc.world!!.playSound(loc,monster.damageSound, 1f,1f)
    }

    @EventHandler
    fun swingArm(event: PlayerInteractEvent) {
        if (!(event.action == Action.LEFT_CLICK_BLOCK || event.action == Action.LEFT_CLICK_AIR)) return
        val player = event.player
        monsterManager.get(player) ?: return
        player.world.playSound(player.location, "minecraft:swing_arm", 0.5f,1f)
    }
    @EventHandler
    fun attack(event: EntityDamageByEntityEvent) {
        val player = if (event.damager is Player) {event.damager as Player} else return
        val monster = monsterManager.get(player) ?: return
        event.isCancelled = true
        val loc = player.eyeLocation.clone()
        val dir = loc.direction
        loc.apply {
            add(dir.multiply(1))
        }
        val box = Util.makeBoundingBox(loc, 1.0)
        val entities = loc.world!!.getNearbyEntities(box)
        entities.forEach {entity ->
            if (entity !is Player) return@forEach
            if (entity == player) return@forEach
            if (monsterManager.contains(entity)) return@forEach
            val customEvent = MonsterAttackPlayerEvent(
                player,
                entity,
                monster
            )
            monsterManager.register(entity)
            Bukkit.getPluginManager().callEvent(customEvent)
        }
    }
}