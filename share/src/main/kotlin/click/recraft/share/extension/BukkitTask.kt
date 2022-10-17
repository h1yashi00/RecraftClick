package click.recraft.share.extension

import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitTask

object TaskGenerator {
    var plugin: JavaPlugin? = null
}

fun runTaskAsync(action: () -> Unit) {
    val plugin = TaskGenerator.plugin
    if (plugin == null) {
        action.invoke()
        return
    }
    val task: BukkitTask.() -> Unit = {
        action.invoke()
    }
    Bukkit.getScheduler().runTaskAsynchronously(plugin, task)
}

fun runTaskSync(action: () -> Unit) {
    val plugin = TaskGenerator.plugin
    if (plugin == null) {
        action.invoke()
        return
    }
    val task: BukkitTask.() -> Unit = {
        action.invoke()
    }
    Bukkit.getScheduler().runTask(plugin, task)
}

fun runTaskLater(delay: Int, action: () -> Unit) {
    val plugin = TaskGenerator.plugin
    if (plugin == null) {
        action.invoke()
        return
    }
    val task: BukkitTask.() -> Unit = {
        action.invoke()
    }
    Bukkit.getScheduler().runTaskLater(plugin, task, delay.toLong())
}
fun runTaskTimer(delay: Int, period: Int, action: () -> Unit) {
    val plugin = TaskGenerator.plugin
    if (plugin == null) {
        action.invoke()
        return
    }
    val task: BukkitTask.() -> Unit = {
        action.invoke()
    }
    Bukkit.getScheduler().runTaskTimer(plugin, task, delay.toLong(), period.toLong())
}