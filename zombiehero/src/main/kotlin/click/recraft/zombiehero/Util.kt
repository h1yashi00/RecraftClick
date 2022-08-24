package click.recraft.zombiehero

import org.bukkit.Location
import org.bukkit.scheduler.BukkitTask
import org.bukkit.util.BoundingBox

object Util {
    fun createTask( task: BukkitTask.() -> Unit): BukkitTask.() -> Unit {
        return task
    }

   fun isHeadLocation(bulletBoundingBox: BoundingBox, headLoc: Location): Boolean {
        val headSize = 0.45
        val x1 = headLoc.x - headSize
        val y1 = headLoc.y - headSize
        val z1 = headLoc.z - headSize

        val x2 = headLoc.x + headSize
        val y2 = headLoc.y + headSize
        val z2 = headLoc.z + headSize
        return BoundingBox(x1,y1,z1, x2,y2,z2).contains(bulletBoundingBox)
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