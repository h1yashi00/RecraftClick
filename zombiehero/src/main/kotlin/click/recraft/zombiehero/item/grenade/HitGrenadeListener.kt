package click.recraft.zombiehero.item.grenade

import click.recraft.zombiehero.Util
import click.recraft.zombiehero.ZombieHero
import click.recraft.zombiehero.item.CustomItemListener
import click.recraft.zombiehero.item.CustomItemManager
import org.bukkit.Bukkit
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.entity.LivingEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.PotionSplashEvent

class HitGrenadeListener(
    manager: CustomItemManager
): CustomItemListener(
    manager
) {
    @EventHandler
    fun hitEntity(event: PotionSplashEvent) {
        val entity = event.entity
        val uuid = entity.uniqueId
        if (!HitGrenade.saveProjectileEntity.contains(uuid)) {
            return
        }
        event.isCancelled = true
        entity.remove()
        HitGrenade.saveProjectileEntity.remove(uuid)
        val loc = entity.location.clone()
        loc.world!!.playSound(loc, Sound.BLOCK_GLASS_BREAK, 1F,1F)
        val fires = arrayListOf<LivingEntity>()
        loc.world!!.getNearbyEntities(Util.makeBoundingBox(loc, 3.5)).forEach {
            if (it !is LivingEntity) return@forEach
            it.damage(10.0)
            it.isVisualFire = true
            fires.add(it)
        }
        (-2..2).forEach {x1 ->
            (-2..2).forEach {z1 ->
                loc.world!!.spawnParticle(Particle.FLAME, loc, 10,1.0,1.0,1.0, 0.0)
            }
        }
        var counter = 10
        val task = Util.createTask {
            if (counter <= 0) {
                fires.forEach { it.isVisualFire = false; }
                cancel()
            }
            fires.forEach { it.noDamageTicks = 0; it.damage(10.0)}
            counter += -1
        }
        Bukkit.getScheduler().runTaskTimer(ZombieHero.plugin, task, 5,1)
    }
}