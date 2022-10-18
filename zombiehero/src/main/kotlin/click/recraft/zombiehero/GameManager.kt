package click.recraft.zombiehero

import click.recraft.share.RedisManager
import click.recraft.share.TeleportServer
import click.recraft.share.database.PlayerManager
import click.recraft.share.extension.runTaskLater
import click.recraft.share.extension.runTaskTimer
import click.recraft.share.protocol.ChannelMessage
import click.recraft.share.protocol.MessageType
import click.recraft.zombiehero.monster.api.MonsterManager
import click.recraft.zombiehero.player.HealthManager
import click.recraft.zombiehero.player.PlayerData
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.GameMode
import org.bukkit.boss.BarColor
import org.bukkit.boss.BarFlag
import org.bukkit.boss.BarStyle
import org.bukkit.entity.Player
import java.util.*

class GameManager {
    val requiredPlayerNum: Int = 2
    var startFlag = false
    val world = GameWorld(Bukkit.getWorld("world")!!)
    val bar = Bukkit.createBossBar("${ChatColor.BOLD}Recraft.Click Monster Hunt ${currentPhase()}", BarColor.YELLOW, BarStyle.SEGMENTED_20, BarFlag.CREATE_FOG)
    private var humanSurvived = false
    private fun currentPhase(): String {
        return "${ZombieHero.plugin.info.currentPhase}/${ZombieHero.plugin.info.maxPhase}"
    }
    private var countdown = false
    fun isCountdowning(): Boolean {
        return countdown
    }

    fun isStart(): Boolean {
        return startFlag
    }

    // 02:00 ← これみたいなフォーマットにするために､作成した関数
    private fun addZero(value: Int): String {
        return if (value / 10 <= 0) {
            "0$value"
        } else {
            "$value"
        }
    }
    private fun timeFormat(time: Int): String {
        val min = time / 60
        val sec = time % 60
        return "${addZero(min)}:${addZero(sec)}"
    }

    private fun humanSurviveTime() {
        val threeMin = 60 * 3
        var countDownHumanSurvive = threeMin
        runTaskTimer(0, 20) {
            if (countDownHumanSurvive <= 0) {
                humanSurvived = true
                checkGameCondition()
                cancel()
            }
            bar.progress = countDownHumanSurvive.toDouble() / threeMin.toDouble()
            bar.setTitle("${ChatColor.BOLD}人間生存まであと${timeFormat(countDownHumanSurvive)} ${currentPhase()}")
            countDownHumanSurvive += -1
        }
    }

    private val playerHaveJoined = mutableSetOf<UUID>()
    fun start() {
        // プレイヤーのゲームのプレイ回数を記録
        startFlag = true
        countdown = true
        humanSurvived = false
        var countDownTime = 20
        runTaskTimer(0, 20) {
            bar.progress = 1.0
            bar.setTitle("${ChatColor.GREEN}${ChatColor.BOLD}ゾンビ${ChatColor.BOLD}を選出しています...(残り${countDownTime}秒)")
            if (countDownTime == 10) {
                Bukkit.getOnlinePlayers().forEach { player ->
                    player.playSound(player.location, "minecraft:countdown", 1f,1f)
                    if (!playerHaveJoined.contains(player.uniqueId)) {
                        playerHaveJoined.add(player.uniqueId)
                        PlayerManager.playGame(player)
                    }
                }
            }
            countDownTime -= 1
            if (countDownTime < 0) {
                runTaskLater( ( 20 * listOf(3,4,5).random())) {
                    if (Bukkit.getOnlinePlayers().size == 1) {
                        RedisManager.publishToBungee(ChannelMessage(MessageType.DELETE))
                        shutdownServer()
                        return@runTaskLater
                    }
                    Bukkit.getOnlinePlayers().forEach { player ->
                        player.playSound(player.location, "minecraft:man_shout", 1f,1f)
                    }
                    chooseEnemy()
                    givePlayerGun()
                    humanSurviveTime()
                    countdown = false
                }
                cancel()
            }
        }
    }
    private fun shutdownServer() {
        Util.broadcastTitle("Game finished !!!!!!!")
        bar.progress = 1.0
        bar.setTitle("${ChatColor.WHITE}FINISH")
        runTaskLater(20 * 3) {
            Bukkit.getOnlinePlayers().forEach { player ->
                TeleportServer.send(player, "lobby", ZombieHero.plugin)
            }
        }
    }
    private fun finish() {
        Bukkit.getScheduler().pendingTasks.forEach {
            if (!ZombieHero.plugin.importantTaskId.contains(it.taskId)) {
                it.cancel()
            }
        }
        val info = RedisManager.serverUpdatePhase()
        if (info == null) {
            shutdownServer()
            return
        }

        HealthManager.clear()
        MonsterManager.clear()
        MapObjectManager.clear()
        PlayerData.clear()
        Bukkit.getOnlinePlayers().forEach { player ->
            player.foodLevel = 20
            player.gameMode = GameMode.SURVIVAL
            player.inventory.clear()
            player.activePotionEffects.forEach {
                player.removePotionEffect(it.type)
            }
            GameMenu.join(player)
            player.teleport(world.randomSpawn())
            player.sendExperienceChange(0f,0)
        }
        world.world.entities.forEach { entity ->
            if (entity is Player) {
                return@forEach
            }
            entity.remove()
        }

        ZombieHero.plugin.info = info
        start()
    }

    // 敵を選出する
    private fun chooseEnemy() {
        val playerNum = Bukkit.getOnlinePlayers().size
        var enemyNum = playerNum / 3
        if (enemyNum <= 1) {
            enemyNum = 1
        }
        repeat(enemyNum) {
            MonsterManager.chooseRandomEnemyMonster()
        }
    }

    private fun givePlayerGun() {
        Bukkit.getOnlinePlayers().forEach { player ->
            if (MonsterManager.contains(player)) return@forEach
            player.inventory.clear()
            player.foodLevel = 20
            val factory = ZombieHero.plugin.customItemFactory
            PlayerManager.get(player).apply {
                player.inventory.addItem (
                    factory.create(itemMain).createItemStack(),
                )
            }
        }
    }


    private fun humanWin() {
        Util.broadcastTitle("${ChatColor.WHITE}Human Win", 20 , 20 * 3,20)
        Bukkit.getOnlinePlayers().forEach { it.playSound(it.location, "zombie_lose_dying", 0.5F ,1F) }
        runTaskLater(20 * 7) {
            finish()
        }
    }
    private fun zombieWin() {
        Util.broadcastTitle("${ChatColor.GREEN}Zombie Win", 20 , 20 * 3,20)
        Bukkit.getOnlinePlayers().forEach { it.playSound(it.location, "zombie_win_laugh", 0.5F ,1F) }
        runTaskLater(20 * 7) {
            finish()
        }
    }
    // ラウンドが終了の場合は､trueを返す
    fun checkGameCondition(): Boolean {
        if (humanSurvived) {
            humanWin()
            return true
        }
        if (MonsterManager.humansNum() == 0) {
            zombieWin()
            return true
        }
        if (MonsterManager.aliveMonsters() == 0) {
            humanWin()
            return true
        }
        return false
    }
}