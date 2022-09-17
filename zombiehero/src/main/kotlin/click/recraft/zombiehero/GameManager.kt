package click.recraft.zombiehero

import click.recraft.zombiehero.player.PlayerData.gun
import org.bukkit.Bukkit

class GameManager {
    val requiredPlayerNum: Int = 2
    var startFlag = false
    private val monsterManager = ZombieHero.plugin.monsterManager
    private val customItemFactory = ZombieHero.plugin.customItemFactory

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

    // 敵を選出する
    private fun chooseEnemy() {
        val playerNum = Bukkit.getOnlinePlayers().size
        var enemyNum = playerNum / 3
        if (enemyNum <= 1) {
            enemyNum = 1
        }
        repeat(enemyNum) {
            monsterManager.chooseRandomEnemyMonster()
        }
    }

    private fun givePlayerGun() {
        Bukkit.getOnlinePlayers().forEach { player ->
            if (monsterManager.contains(player)) return@forEach
            val type = player.gun()
            val gun = customItemFactory.createGun(type)
            player.inventory.addItem(gun.createItemStack())
        }
    }
}