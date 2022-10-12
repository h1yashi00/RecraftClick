package click.recraft.zombiehero.item.skill.grenade

import click.recraft.zombiehero.Util
import click.recraft.zombiehero.item.CustomItemListener
import click.recraft.zombiehero.item.CustomItemManager
import click.recraft.zombiehero.player.HealthManager.damagePluginHealth
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
        val thrownPotion = event.entity
        val hitGrenade = getItem(thrownPotion.item) ?: return
        val uuid = thrownPotion.uniqueId
        if (!HitGrenade.saveProjectileEntity.contains(uuid)) {
            return
        }
        event.isCancelled = true
        thrownPotion.remove()
        HitGrenade.saveProjectileEntity.remove(uuid)
        val loc = thrownPotion.location.clone()
        loc.world!!.playSound(loc, Sound.BLOCK_GLASS_BREAK, 1F,1F)
        loc.world!!.getNearbyEntities(Util.makeBoundingBox(loc, 3.5)).forEach { hitEntity ->
            if (hitEntity !is LivingEntity) return@forEach
            hitEntity.damagePluginHealth(thrownPotion.shooter as LivingEntity,  10, hitGrenade)
        }
        (-2..2).forEach {x1 ->
            (-2..2).forEach {z1 ->
                loc.world!!.spawnParticle(Particle.FLAME, loc, 10,1.0,1.0,1.0, 0.0)
            }
        }
    }
}