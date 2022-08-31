package click.recraft.zombiehero.item.grenade

import click.recraft.zombiehero.Util
import click.recraft.zombiehero.gun.api.Tick
import org.bukkit.Sound

class GrenadeImpl: Grenade(
    "creeper",
    Tick.sec(1.5),
    {loc ->
        val entities = loc.world!!.getNearbyEntities(Util.makeBoundingBox(loc, 5.0))
        loc.world!!.playSound(loc, Sound.ENTITY_GENERIC_EXPLODE, 0.5f,0.5f)
        entities.forEach {
            val vec = it.location.apply { y += 1 }.toVector().subtract(loc.toVector())
            if (vec.x == 0.0 && vec.y == 0.0 && vec.z == 0.0) return@forEach
            it.velocity = vec.normalize().multiply(1.8)
        }
    }
)