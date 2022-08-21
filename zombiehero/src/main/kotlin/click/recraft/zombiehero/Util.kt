package click.recraft.zombiehero

import org.bukkit.Location
import org.bukkit.util.BoundingBox

object Util {
    fun makeBoundingBox(loc: Location, hitbox: Double): BoundingBox {
        val x1 = loc.x - hitbox
        val y1 = loc.y - hitbox
        val z1 = loc.z - hitbox

        val x2 = loc.x + hitbox
        val y2 = loc.y + hitbox
        val z2 = loc.z + hitbox
        return BoundingBox(x1, y1, z1, x2, y2, z2)
    }
}