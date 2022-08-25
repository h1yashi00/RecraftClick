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
        if (gunStats.reloading) {
            return
        }
        if (gunStats.totalArmo <= 0) {
            return
        }
        if (gunStats.currentArmo == armo) {
            return
        }
        gunStats.reloading = true
        player.playSound(player.location, Sound.BLOCK_WOODEN_DOOR_OPEN, 1f,2f)
        val task = Util.createTask {
            if (checkTick <= 0) {
                val shootArmo = armo - gunStats.currentArmo
                val restoreArmo = if (shootArmo <= gunStats.totalArmo) {
                    shootArmo
                }
                else {
                    gunStats.totalArmo
                }
                gunStats.totalArmo   +=  -restoreArmo
                gunStats.currentArmo +=   restoreArmo
                gunStats.reloading = false
                val reloadItem = player.inventory.itemInMainHand.clone()
                    .apply {
                        amount = gunStats.currentArmo
                    }
                player.inventory.setItemInMainHand(reloadItem)
                player.sendExperienceChange(1.0F, gunStats.totalArmo)
                cancel()
                return@createTask
            }

            if (item != player.inventory.itemInMainHand) {
                gunStats.reloading = false
                cancel()
                return@createTask
            }
            player.playSound(player.location, Sound.BLOCK_NOTE_BLOCK_HARP, 0.5f, 2f)
            checkTick += -1
        }
        Bukkit.getScheduler().runTaskTimer(ZombieHero.plugin, task, 0,1)
    }
}