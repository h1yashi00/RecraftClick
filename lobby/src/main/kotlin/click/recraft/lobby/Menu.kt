package click.recraft.lobby

import click.recraft.share.*
import click.recraft.share.protocol.ServerInfo
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.scheduler.BukkitTask

object Menu {
    fun load() {
    }

    private lateinit var servers: MutableSet<ServerInfo>
    private fun updateServers() {
        servers = RedisManager.getServers()
    }

    val item = Main.plugin.interactItem(
        item(Material.EMERALD, displayName = "${ChatColor.BOLD}サーバ選択"),
        rightClick = true,
        leftClick = false
    ) {
        val task: BukkitTask.() -> Unit = {
            updateServers()
            println("rightClick2")

            val syncTask: BukkitTask.() ->Unit = {
                player.openInventory(serverSelect.createInv(player))
            }
            Bukkit.getScheduler().runTask(Main.plugin, syncTask)
        }
        println("rightClick")
        Bukkit.getScheduler().runTaskAsynchronously(Main.plugin, task)
    }

    private val serverSelect = Main.plugin.menu(
        "SelectServer",
        true,
    ) {
        slot(0,1, item(Material.COAL)) {
            onClick {
                player.sendMessage("$servers")
            }
        }
    }
}