package click.recraft.share

import org.bukkit.scheduler.BukkitTask

object Util {
    fun createTask( task: BukkitTask.() -> Unit): BukkitTask.() -> Unit {
        return task
    }
}