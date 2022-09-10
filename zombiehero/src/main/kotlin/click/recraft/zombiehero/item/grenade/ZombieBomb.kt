package click.recraft.zombiehero.item.grenade

import click.recraft.zombiehero.Util
import click.recraft.zombiehero.gun.api.Tick
import org.bukkit.Location
import org.bukkit.Sound
import org.bukkit.entity.Item
import org.bukkit.entity.Player

class ZombieBomb: Grenade(
    "ZombieBomb",
    Tick.sec(1.5),
    200,
    Tick.sec(30.0),
    999999
) {
    override fun pickUp(player: Player, item: Item) {
    }

    override fun explosion(location: Location) {
        val entities = location.world!!.getNearbyEntities(Util.makeBoundingBox(location, 5.0))
        location.world!!.playSound(location, Sound.ENTITY_GENERIC_EXPLODE, 0.5f,0.5f)
        entities.forEach {
            val vec = it.location.apply { y += 1 }.toVector().subtract(location.toVector())
            if (vec.x == 0.0 && vec.y == 0.0 && vec.z == 0.0) return@forEach
            it.velocity = vec.normalize().multiply(1.8)
        }
    }
}