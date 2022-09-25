package click.recraft.zombiehero.player

import click.recraft.zombiehero.event.BulletHitLivingEntityEvent
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
        val shooter = event.shooter
        if (shooter !is Player) return
        world.spawnParticle(Particle.BLOCK_CRACK, bullet, 10, Material.REDSTONE_BLOCK.createBlockData())
    }
}