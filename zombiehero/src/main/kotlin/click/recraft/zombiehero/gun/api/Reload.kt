package click.recraft.zombiehero.gun.api

import click.recraft.zombiehero.Util
import click.recraft.zombiehero.ZombieHero
import click.recraft.zombiehero.gun.base.Tick
import click.recraft.zombiehero.item.PlayerGun
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.entity.Player

interface Reload {
    val armo: Int
    // プレイヤーチェックするのが1sec/20tick の 1tick毎なので､ 50msでチェック
    val reloadTime: Tick

    fun reload(player: Player, gunStats: PlayerGun.GunStats) {
        val item = player.inventory.itemInMainHand
        var checkTick = reloadTime.tick
        if (gunStats.totalArmo < 0) {
            return
        }
        gunStats.reloading = true
        val task = Util.createTask {
            if (checkTick == 0) {
                val restoreArmo = if (armo <= gunStats.totalArmo) {
                    armo
                }
                else {
                    gunStats.totalArmo
                }
                gunStats.totalArmo =- restoreArmo
                gunStats.currentArmo += restoreArmo
                gunStats.reloading = false
                cancel()
                return@createTask
            }

            if (item != player.inventory.itemInMainHand) {
                cancel()
                return@createTask
            }

            player.playSound(player.location, Sound.BLOCK_NOTE_BLOCK_HARP, 1f, 1f)
            checkTick =- 1
        }
        Bukkit.getScheduler().runTaskTimer(ZombieHero.plugin, task, 0,1)
        player.inventory.remove(item)
    }
}