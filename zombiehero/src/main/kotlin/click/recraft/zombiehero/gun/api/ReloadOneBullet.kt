package click.recraft.zombiehero.gun.api

import click.recraft.zombiehero.Util
import click.recraft.zombiehero.ZombieHero
import click.recraft.zombiehero.gun.base.Tick
import click.recraft.zombiehero.item.PlayerGun
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.entity.Player

class ReloadOneBullet (
    override val armo: Int,
    override val reloadTime: Tick
) : Reload {
    override fun reload(player: Player, gunStats: PlayerGun.GunStats) {
        if (!check(gunStats)) {
            return
        }
        gunStats.reloading = true
        val item = player.inventory.itemInMainHand
        player.playSound(player.location, Sound.BLOCK_WOODEN_DOOR_OPEN, 1f,2f)
        val task = Util.createTask {
            // リロードの途中で､プレイヤーが射撃したことにより､reloadが止まった｡
            if (!gunStats.reloading) {
                return@createTask
            }
            if (player.inventory.itemInMainHand != item) {
                gunStats.reloading = false
                return@createTask
            }
            gunStats.reloading = false
            gunStats.totalArmo += -1
            gunStats.currentArmo += 1
            player.playSound(player.location, Sound.BLOCK_WOODEN_DOOR_OPEN, 1f,2f)
            player.sendExperienceChange(1F, gunStats.totalArmo)
            val reloadItem = player.inventory.itemInMainHand.clone()
                .apply {
                    amount = gunStats.currentArmo
                }
            player.inventory.setItemInMainHand(reloadItem)
            reload(player, gunStats)
        }
        Bukkit.getScheduler().runTaskLater(ZombieHero.plugin, task, reloadTime.tick.toLong())
    }
}