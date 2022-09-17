package click.recraft.zombiehero

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.EntityType
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractAtEntityEvent
import org.bukkit.event.player.PlayerJoinEvent

class PlayerJoin: Listener {
    @EventHandler
    fun playerInteract(event: PlayerInteractAtEntityEvent) {
        if (event.rightClicked.type != EntityType.ARMOR_STAND) return
        event.isCancelled = true
    }
    @EventHandler
    fun joinPlayer(event: PlayerJoinEvent) {
        val player = event.player
        player.foodLevel = 20
        player.inventory.clear()
        val playerNum = Bukkit.getOnlinePlayers().size
        event.joinMessage = "${ChatColor.YELLOW}プレイヤーが参加しました｡ 2人以上で開始します｡ 現在の参加人数: ${playerNum}/32"
        val game=  ZombieHero.plugin.gameManager
        if (game.isStart()) {
            return
        }
        if (playerNum >= game.requiredPlayerNum) {
            game.start()
        }
    }
}