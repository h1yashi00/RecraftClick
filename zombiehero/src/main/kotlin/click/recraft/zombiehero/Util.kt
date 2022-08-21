package click.recraft.zombiehero

import org.bukkit.Location
import org.bukkit.scheduler.BukkitTask
import org.bukkit.util.BoundingBox

object Util {
    fun createTask( task: BukkitTask.() -> Unit): BukkitTask.() -> Unit {
        return task
    }
    fun makeBoundingBox(loc: Location, hitBox: Double): BoundingBox {
        val x1 = loc.x - hitBox
        val y1 = loc.y - hitBox
        val z1 = loc.z - hitBox

        val x2 = loc.x + hitBox
        val y2 = loc.y + hitBox
        val z2 = loc.z + hitBox
        return BoundingBox(x1, y1, z1, x2, y2, z2)
    }
}