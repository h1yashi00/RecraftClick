package click.recraft.zombiehero.item.grenade

import click.recraft.zombiehero.Util
import click.recraft.zombiehero.gun.api.Tick
import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.entity.Item
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player

class NormalGrenade : Grenade(
    "普通のグレネード",
    Tick.sec(1.5),
    201,
    useDelayTick = Tick.sec(30.0),
    99999
) {
    override fun pickUp(player: Player, item: Item) {
    }

    override fun explosion(location: Location) {
        val loc = location.clone()
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
}