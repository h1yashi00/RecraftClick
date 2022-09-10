package click.recraft.zombiehero.item.grenade

import click.recraft.zombiehero.Util
import click.recraft.zombiehero.gun.api.Tick
import org.bukkit.Location
import org.bukkit.Sound

class ZombieGrenadeTouch: TouchGrenade (
    "ZombieGrenadeTouch",
    explosionDelay = Tick.sec(3.0),
    201,
    useDelayTick = Tick.sec(1.0),
    pickUpDelay = 20 * 1
) {
    override fun explosion(location: Location) {
        if (touched) return
        val entities = location.world!!.getNearbyEntities(Util.makeBoundingBox(location, 5.0))
        location.world!!.playSound(location, Sound.ENTITY_GENERIC_EXPLODE, 0.5f,0.5f)
        entities.forEach {
            val vec = it.location.apply { y += 1 }.toVector().subtract(location.toVector())
            if (vec.x == 0.0 && vec.y == 0.0 && vec.z == 0.0) return@forEach
            it.velocity = vec.normalize().multiply(1.8)
        }
    }
}