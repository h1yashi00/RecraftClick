package click.recraft.lobby

import click.recraft.share.*
import click.recraft.share.extension.runTaskAsync
import click.recraft.share.extension.runTaskSync
import click.recraft.share.protocol.ChannelMessage
import click.recraft.share.protocol.MessageType
import click.recraft.share.protocol.ServerInfo
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory

object MenuServerSelect: Listener {
    fun load() {
    }

    private var servers: MutableList<ServerInfo> = mutableListOf()
    private var lastChecked = System.currentTimeMillis()
    private fun updateServers() {
        val now = System.currentTimeMillis()
        if (lastChecked + (1000 * 5) > now) {
            return
        }
        lastChecked = now
        servers = RedisManager.getServers().values.toMutableList()
        Bukkit.getOnlinePlayers().forEach { if (it.isOp) {it.sendMessage("servers: $servers")} }
        if (servers.size == 0) {
            Bukkit.broadcastMessage("${ChatColor.GRAY}Creating Server...")
            val msg = ChannelMessage(
                MessageType.CREATE
            )
            RedisManager.publishToBungee(msg)
        }
    }

    val serverSelectItem = Main.plugin.interactItem(
        item(Material.COMPASS, displayName = "${ChatColor.BOLD}サーバ選択"),
        rightClick = true,
        leftClick = false
    ) {
        runTaskAsync {
            updateServers()
            if (servers.size == 0) return@runTaskAsync
            runTaskSync {
                player.openInventory(dynamicInv())
            }
        }
    }
    @EventHandler
    fun playerClickMenuEvent(event: InventoryClickEvent) {
        val player = event.whoClicked as Player
        val title = event.view.title
        if (title != this.title) return
        event.isCancelled = true
        val item = event.currentItem ?: return
        val name = item.itemMeta?.displayName ?: return
        val containerId = name.removePrefix(prefix)
        TeleportServer.send(player, containerId, Main.plugin)
    }

    private const val title = "Select Server"
    private val prefix = "${ChatColor.WHITE}${ChatColor.BOLD}server "

    private fun dynamicInv(): Inventory {
        val inv = Bukkit.createInventory(null, 9, title)
        servers.forEachIndexed { i, serverInfo ->
            if (serverInfo.currentPhase == 0) return@forEachIndexed
            inv.addItem(
                item(
                    Material.LIME_DYE,
                    displayName = "$prefix${serverInfo.containerId}",
                    lore = listOf(
                        "${ChatColor.WHITE}player: ${serverInfo.currentPlayerNum} / ${serverInfo.maxPlayerNum}",
                        "${ChatColor.WHITE}phase:  ${serverInfo.currentPhase} / ${serverInfo.maxPhase}",
                    )
                )
            )
        }
        return inv
    }
}