package click.recraft.zombiehero

import click.recraft.zombiehero.monster.api.MonsterManager
import org.bukkit.ChatColor
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent

class PlayerQuit: Listener {
    @EventHandler
    fun quit(event: PlayerQuitEvent) {
        val player = event.player
        event.quitMessage = "${ChatColor.YELLOW}${player.name}が退出しました｡"
        val gameManager = ZombieHero.plugin.gameManager
        // ゲームが人数に達していない場合は､特別な処理を行わない｡
        if (!gameManager.isStart()) return
        // クルールダウンの際は､特別な処理を行わない
        if (gameManager.isCountdowning()) {
            return
        }
        // ゲーム中に退出したので､特別な処理(ゾンビじゃない場合はゾンビに､ゾンビの場合は､存在を消す)
        if (!MonsterManager.contains(player)) {
            MonsterManager.register(player)
        }
        else {
            MonsterManager.remove(player)
        }
        gameManager.checkGameCondition()
    }
}