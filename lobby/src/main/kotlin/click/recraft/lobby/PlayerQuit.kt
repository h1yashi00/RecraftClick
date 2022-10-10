package click.recraft.lobby

import click.recraft.share.protocol.Database
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent

class PlayerQuit: Listener {
    @EventHandler
    fun playerQuit(event: PlayerQuitEvent) {
        val player = event.player
        Database.quitUpdate(player)
        Database.removePlayerData(player)
    }
}