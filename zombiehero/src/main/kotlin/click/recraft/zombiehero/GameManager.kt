package click.recraft.zombiehero

import click.recraft.zombiehero.monster.api.MonsterManager
import click.recraft.zombiehero.player.HealthManager
import click.recraft.zombiehero.player.PlayerData.gun
import org.bukkit.Bukkit

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
    fun finish() {
        Bukkit.getOnlinePlayers().forEach { player ->
            Bukkit.getScheduler().pendingTasks.forEach {
                if (!ZombieHero.plugin.importantTaskId.contains(it.taskId)) {
                    it.cancel()
                }
            }
            player.inventory.clear()
            player.activePotionEffects.forEach {
                player.removePotionEffect(it.type)
            }
            HealthManager.clear()
            MonsterManager.clear()
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
            val type = player.gun()
            val gun = customItemFactory.createGun(type)
            player.inventory.addItem(gun.createItemStack())
        }
    }
}