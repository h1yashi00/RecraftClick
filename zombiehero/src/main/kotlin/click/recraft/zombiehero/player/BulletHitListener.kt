package click.recraft.zombiehero.player

import click.recraft.zombiehero.event.BulletHitLivingEntityEvent
import click.recraft.zombiehero.player.HealthManager.getPluginHealth
import click.recraft.zombiehero.player.PlayerData.removeHeadShot
import click.recraft.zombiehero.player.PlayerData.setHeadShot
import click.recraft.zombiehero.player.PlayerData.setShooter
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener

class BulletHitListener: Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    fun hit(event: BulletHitLivingEntityEvent) {
        val hit = event.hitLivingEntity
        val world = hit.world
        val bullet = event.bulletLocation
        event.damage = if (event.isHeadShot) {
            event.damage * 2
        } else {
            event.damage
        }
        world.spawnParticle(Particle.BLOCK_CRACK, bullet, 10, Material.REDSTONE_BLOCK.createBlockData())
    }

    @EventHandler(priority = EventPriority.MONITOR)
    fun hit1(event: BulletHitLivingEntityEvent) {
        val damage = event.damage
        val hit = event.hitLivingEntity
        val player = if (hit is Player) {hit} else return
        val health = player.getPluginHealth()
        if (health - damage <= 0) {
            player.setShooter(event.shooter as Player)
            if (event.isHeadShot) {
                player.setHeadShot()
            }
            else {
                player.removeHeadShot()
            }
        }
    }
}