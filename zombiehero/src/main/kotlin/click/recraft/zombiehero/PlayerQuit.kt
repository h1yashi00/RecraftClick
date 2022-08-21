package click.recraft.zombiehero

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent

class PlayerQuit: Listener {
    @EventHandler
    fun quit(event: PlayerQuitEvent) {
        val player = event.player
//        ZombieHeroPlayerData.remove(player.uniqueId)
    }
}