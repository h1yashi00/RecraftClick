package click.recraft.zombiehero

import click.recraft.share.RedisManager
import click.recraft.share.TeleportServer
import click.recraft.share.item
import click.recraft.zombiehero.item.CustomItemFactory
import click.recraft.zombiehero.monster.api.MonsterManager
import click.recraft.zombiehero.player.HealthManager
import click.recraft.zombiehero.player.PlayerData
import click.recraft.zombiehero.player.PlayerData.grenade
import click.recraft.zombiehero.player.PlayerData.mainGunType
import click.recraft.zombiehero.player.PlayerData.subGunType
import click.recraft.zombiehero.player.PlayerData.sword
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.boss.BarColor
import org.bukkit.boss.BarFlag
import org.bukkit.boss.BarStyle
import org.bukkit.entity.Player

class GameManager {
    val requiredPlayerNum: Int = 2
    var startFlag = false
    private val customItemFactory = ZombieHero.plugin.customItemFactory
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
        val task = Util.createTask {
            if (countDownHumanSurvive <= 0) {
                humanSurvived = true
                checkGameCondition()
                cancel()
            }
            bar.progress = countDownHumanSurvive.toDouble() / threeMin.toDouble()
            bar.setTitle("${ChatColor.BOLD}人間生存まであと${timeFormat(countDownHumanSurvive)} ${currentPhase()}")
            countDownHumanSurvive += -1
        }
        Bukkit.getScheduler().runTaskTimer(ZombieHero.plugin, task, 0, 20)
    }

    fun start() {
        startFlag = true
        countdown = true
        humanSurvived = false
        var countDownTime = 20
        val task = Util.createTask {
            bar.progress = 1.0
            bar.setTitle("${ChatColor.GREEN}${ChatColor.BOLD}ゾンビ${ChatColor.BOLD}を選出しています...(残り${countDownTime}秒)")
            if (countDownTime == 10) {
                Bukkit.getOnlinePlayers().forEach { it.playSound(it.location, "minecraft:countdown", 1f,1f) }
            }
            countDownTime -= 1
            if (countDownTime < 0) {
                val lateTask = Util.createTask {
                    if (Bukkit.getOnlinePlayers().size == 1) {shutdownServer(); return@createTask}
                    Bukkit.getOnlinePlayers().forEach { player ->
                        player.playSound(player.location, "minecraft:man_shout", 1f,1f)
                    }
                    chooseEnemy()
                    givePlayerGun()
                    humanSurviveTime()
                    countdown = false
                }
                Bukkit.getScheduler().runTaskLater(ZombieHero.plugin, lateTask, (20 * listOf(3,4,5).random()).toLong())

                cancel()
            }
        }
        Bukkit.getScheduler().runTaskTimer(ZombieHero.plugin, task, 0, 20)
    }
    private fun shutdownServer() {
        Util.broadcastTitle("Game finished !!!!!!!")
        bar.progress = 1.0
        bar.setTitle("${ChatColor.WHITE}FINISH")
        val task = Util.createTask {
            Bukkit.getOnlinePlayers().forEach { player ->
                TeleportServer.send(player, "lobby", ZombieHero.plugin)
            }
            val lateTask = Util.createTask {
                ZombieHero.plugin.onDisable()
            }
            Bukkit.getScheduler().runTaskLater(ZombieHero.plugin, lateTask, 20 * 3)
        }
        Bukkit.getScheduler().runTaskLater(ZombieHero.plugin, task, 20 * 3)
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
            val mainGun = customItemFactory.createMainGun(player.mainGunType())
            val subGun = customItemFactory.createSubGun(player.subGunType())
            val sword = customItemFactory.createSword(player.sword())
            val grenade = customItemFactory.createGrenade(player.grenade())
            player.inventory.setItem(0, mainGun.createItemStack())
            player.inventory.setItem(1, subGun.createItemStack())
            player.inventory.setItem(2, sword.createItemStack())
            player.inventory.setItem(3, grenade.createItemStack())
            player.inventory.setItem(4, item(Material.DIAMOND_PICKAXE))
            player.inventory.setItem(31, item(Material.DIAMOND_AXE))
            player.inventory.setItem(5, customItemFactory.createSkillItem(CustomItemFactory.SkillType.SPEED_UP).createItemStack())
            player.inventory.setItem(6, customItemFactory.createSkillItem(CustomItemFactory.SkillType.HEADSHOT).createItemStack())
        }
    }


    private fun humanWin() {
        Util.broadcastTitle("${ChatColor.WHITE}Human Win", 20 , 20 * 3,20)
        Bukkit.getOnlinePlayers().forEach { it.playSound(it.location, "zombie_lose_dying", 0.5F ,1F) }
        val task = Util.createTask {
            finish()
        }
        Bukkit.getScheduler().runTaskLater(ZombieHero.plugin, task, 20 * 7)
    }
    private fun zombieWin() {
        Util.broadcastTitle("${ChatColor.GREEN}Zombie Win", 20 , 20 * 3,20)
        Bukkit.getOnlinePlayers().forEach { it.playSound(it.location, "zombie_win_laugh", 0.5F ,1F) }
        val task = Util.createTask {
            finish()
        }
        Bukkit.getScheduler().runTaskLater(ZombieHero.plugin, task, 20 * 7)
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