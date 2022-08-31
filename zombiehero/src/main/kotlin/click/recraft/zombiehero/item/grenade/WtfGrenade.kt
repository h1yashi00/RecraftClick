package click.recraft.zombiehero.item.grenade

import click.recraft.zombiehero.Util
import click.recraft.zombiehero.gun.api.Tick
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.entity.LivingEntity

class WtfGrenade: Grenade(
    "普通のグレネード",
    Tick.sec(1.5),
    { loc ->
        val entities = loc.world!!.getNearbyEntities(Util.makeBoundingBox(loc, 5.0)) {
            it is LivingEntity
        }
        loc.world!!.playSound(loc, Sound.ENTITY_GENERIC_EXPLODE, 0.5f, 0.5f)
        loc.world!!.spawnParticle(Particle.EXPLOSION_HUGE, loc, 1)
        entities.forEach { entity ->
            entity as LivingEntity
            val dis = entity.location.distance(loc)
            val baseDamage = 500.0
            val damage = when {
                dis <= 1 -> baseDamage
                dis >= 5 -> baseDamage / 5
                else -> {
                    baseDamage / dis
                }
            }
            entity.damage(damage)
        }
    }
)