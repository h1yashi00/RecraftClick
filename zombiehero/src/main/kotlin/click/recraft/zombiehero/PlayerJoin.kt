package click.recraft.zombiehero

import click.recraft.share.database.PlayerManager
import click.recraft.zombiehero.monster.api.MonsterManager
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.GameMode
import org.bukkit.entity.EntityType
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractAtEntityEvent
import org.bukkit.event.player.PlayerJoinEvent
import java.util.*

class PlayerJoin: Listener {
    private val playerHaveJoined = mutableSetOf<UUID>()
    @EventHandler
    fun playerInteract(event: PlayerInteractAtEntityEvent) {
        if (event.rightClicked.type != EntityType.ARMOR_STAND) return
        event.isCancelled = true
    }
    @EventHandler
    fun joinPlayer(event: PlayerJoinEvent) {
        val player = event.player
        PlayerManager.login(player)
        // プレイヤーのゲームのプレイ回数を記録
        if (!playerHaveJoined.contains(player.uniqueId)) {
            playerHaveJoined.add(player.uniqueId)
            PlayerManager.playGame(player)
        }
        player.gameMode = GameMode.SURVIVAL
        player.foodLevel = 20
        player.inventory.clear()
        val playerNum = Bukkit.getOnlinePlayers().size
        player.teleport(ZombieHero.plugin.gameManager.world.randomSpawn())
        val task = Util.createTask {
            GameMenu.join(player)
        }
        Bukkit.getScheduler().runTaskLater(ZombieHero.plugin, task, 1)
        ZombieHero.plugin.gameManager.bar.addPlayer(player)
        val game=  ZombieHero.plugin.gameManager
        if (game.isStart()) {
            // 途中参加してきたプレイヤーで､すでにゲームが始まっている場合､敵チームに入れて､スペクテイターにする.
            if (!game.isCountdowning()) {
                MonsterManager.register(player)
                player.gameMode = GameMode.SPECTATOR
            }
            event.joinMessage = "${ChatColor.YELLOW}${player.name}が参加しました"
            return
        }
        event.joinMessage = "${ChatColor.YELLOW}${player.name}が参加しました｡ 2人以上で開始します｡ 現在の参加人数: ${playerNum}/32"
        if (playerNum >= game.requiredPlayerNum) {
            game.start()
        }
    }
}