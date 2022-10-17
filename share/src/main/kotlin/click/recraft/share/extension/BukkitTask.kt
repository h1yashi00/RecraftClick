package click.recraft.share.extension

import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitTask

object TaskGenerator {
    var plugin: JavaPlugin? = null
}

fun runTaskAsync(action: () -> Unit) {
    val task: BukkitTask.() -> Unit = {
        action.invoke()
    }
    Bukkit.getScheduler().runTaskAsynchronously(TaskGenerator.plugin!!, task)
}

fun runTaskSync(action: () -> Unit) {
    val task: BukkitTask.() -> Unit = {
        action.invoke()
    }
    Bukkit.getScheduler().runTask(TaskGenerator.plugin!!, task)
}

fun runTaskLater(delay: Int, action: () -> Unit) {
    val task: BukkitTask.() -> Unit = {
        action.invoke()
    }
    Bukkit.getScheduler().runTaskLater(TaskGenerator.plugin!!, task, delay.toLong())
}
fun runTaskTimer(delay: Int, period: Int, action: () -> Unit) {
    val task: BukkitTask.() -> Unit = {
        action.invoke()
    }
    Bukkit.getScheduler().runTaskTimer(TaskGenerator.plugin!!, task, delay.toLong(), period.toLong())
}