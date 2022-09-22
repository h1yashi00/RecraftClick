package click.recraft.zombiehero

import click.recraft.zombiehero.item.CustomItemFactory
import click.recraft.zombiehero.monster.api.MonsterManager
import click.recraft.zombiehero.player.HealthManager
import click.recraft.zombiehero.player.PlayerData
import click.recraft.zombiehero.player.PlayerData.mainGunType
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.GameMode

class GameManager {
    val requiredPlayerNum: Int = 2
    var startFlag = false
    private val customItemFactory = ZombieHero.plugin.customItemFactory
    val world = GameWorld(Bukkit.getWorld("world")!!)

    fun isStart(): Boolean {
        return startFlag
    }



    fun start() {
        startFlag = true
        var countDownTime = 20
        val task = Util.createTask {
            Util.broadcastTitle("ゾンビを選出します(残り${countDownTime}秒)")
            if (countDownTime == 10) {
                Bukkit.getOnlinePlayers().forEach { it.playSound(it.location, "minecraft:countdown", 1f,1f) }
            }
            countDownTime -= 1
            if (countDownTime < 0) {
                val lateTask = Util.createTask {
                    Bukkit.getOnlinePlayers().forEach { it.playSound(it.location, "minecraft:man_shout", 1f,1f) }
                    chooseEnemy()
                    givePlayerGun()
                }
                Bukkit.getScheduler().runTaskLater(ZombieHero.plugin, lateTask, (20 * listOf(3,4,5).random()).toLong())
                cancel()
            }
        }
        Bukkit.getScheduler().runTaskTimer(ZombieHero.plugin, task, 0, 20)
    }
    private fun finish() {
        Bukkit.getOnlinePlayers().forEach { player ->
            player.foodLevel = 20
            Bukkit.getScheduler().pendingTasks.forEach {
                if (!ZombieHero.plugin.importantTaskId.contains(it.taskId)) {
                    it.cancel()
                }
            }
            player.sendExperienceChange(0f,0)
            player.gameMode = GameMode.SURVIVAL
            player.inventory.clear()
            player.activePotionEffects.forEach {
                player.removePotionEffect(it.type)
            }
            HealthManager.clear()
            MonsterManager.clear()
            SpawnedEntityManager.clear()
            PlayerData.clear()
            player.inventory.addItem(
                GameMenu.mainGunSelect.getItem(),
                GameMenu.subGunSelect.getItem(),
                GameMenu.zombieSelect.getItem(),
            )
            player.teleport(world.randomSpawn())
        }
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
            val type = player.mainGunType()
            val gun = customItemFactory.createMainGun(type)
            player.inventory.addItem(gun.createItemStack())
            player.inventory.addItem(customItemFactory.createSkillItem(CustomItemFactory.SkillType.SPEED_UP).createItemStack())
            player.inventory.addItem(customItemFactory.createSkillItem(CustomItemFactory.SkillType.HEADSHOT).createItemStack())
        }
    }

    // ラウンドが終了の場合は､trueを返す
    fun checkGameCondition(): Boolean {
        if (MonsterManager.humansNum() == 0) {
            Util.broadcastTitle("${ChatColor.GREEN}Zombie Win", 20 , 20 * 3,20)
            Bukkit.getOnlinePlayers().forEach { it.playSound(it.location, "zombie_win_laugh", 0.5F ,1F) }
            val task = Util.createTask {
                finish()
            }
            Bukkit.getScheduler().runTaskLater(ZombieHero.plugin, task, 20 * 7)
            return true
        }
        if (MonsterManager.aliveMonsters() == 0) {
            Util.broadcastTitle("${ChatColor.WHITE}Human Win", 20 , 20 * 3,20)
            Bukkit.getOnlinePlayers().forEach { it.playSound(it.location, "zombie_lose_dying", 0.5F ,1F) }
            val task = Util.createTask {
                finish()
            }
            Bukkit.getScheduler().runTaskLater(ZombieHero.plugin, task, 20 * 7)
            return true
        }
        return false
    }
}