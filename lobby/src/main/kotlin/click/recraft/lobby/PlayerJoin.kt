package click.recraft.lobby

import click.recraft.share.database.PlayerManager
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.GameMode
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class PlayerJoin : Listener {
    @EventHandler
    fun join(event: PlayerJoinEvent) {
        val player = event.player
        player.inventory.clear()
        player.inventory.addItem(
            MenuServerSelect.serverSelectItem.getItem(),
            MenuPlayerStats.item.getItem(),
            MenuPlayerZombieHeroStats.item.getItem()
        )
        player.gameMode = GameMode.SURVIVAL
        val world = Bukkit.getWorld("world")!!
        event.joinMessage = "${ChatColor.YELLOW}${player.name}が参加しました"
        player.teleport(world.spawnLocation)
        PlayerManager.login(player)
    }
}