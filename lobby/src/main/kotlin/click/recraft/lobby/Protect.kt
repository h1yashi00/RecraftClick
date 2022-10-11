package click.recraft.lobby

import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockFromToEvent
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.event.entity.EntityChangeBlockEvent
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityExplodeEvent
import org.bukkit.event.player.PlayerInteractAtEntityEvent

class Protect: Listener {
    @EventHandler
    fun playerBreakBlock(event: BlockBreakEvent) {
        val player = event.player
        event.isCancelled = !player.isOp
    }
    @EventHandler
    fun playerAttack(event: EntityDamageByEntityEvent) {
        if (event.damager is Player) {
            if (event.damager.isOp) {
                return
            }
        }
        event.isCancelled = true
    }
    @EventHandler
    fun block(event: EntityChangeBlockEvent) {
        val entity = event.entity
        if (entity is Player) {
            if (entity.isOp) {
                return
            }
        }
        event.isCancelled = true
    }
    @EventHandler
    fun blockBreak(event: EntityExplodeEvent) {
        event.isCancelled = true
    }
    @EventHandler
    fun spawn(event: CreatureSpawnEvent) {
        if (event.spawnReason == CreatureSpawnEvent.SpawnReason.NATURAL) {
            event.isCancelled = true
        }
    }
    @EventHandler
    fun waterFlow(event: BlockFromToEvent) {
        event.isCancelled = true
    }
    @EventHandler
    fun damage(event: EntityDamageEvent) {
        val player = if (event.entity is Player) {event.entity as Player}  else return
        if (event.cause == EntityDamageEvent.DamageCause.VOID) {
            event.isCancelled = true
            player.teleport(player.world.spawnLocation)
        }
        if (event.cause == EntityDamageEvent.DamageCause.FALL) {
            event.isCancelled = true
        }
    }

    @EventHandler
    fun click(event: PlayerInteractAtEntityEvent) {
        val player = event.player
        if (player.isOp) {
            return
        }
        val entity = event.rightClicked
        if (entity.type == EntityType.ARMOR_STAND) {
            event.isCancelled = true
        }
    }
}