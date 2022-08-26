package click.recraft.zombiehero.gun.api

import click.recraft.zombiehero.Util
import click.recraft.zombiehero.ZombieHero
import click.recraft.zombiehero.item.PlayerGun
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.entity.Player

class ReloadFullBullet(
    override var armo : Int = 0,
    override var reloadTime: Tick = Tick.sec(0.0),
    override val reloadManager: ReloadManager,
) : Reload {
    override fun reload(player: Player, gun: PlayerGun) {
        val gunStats = gun.stats
        val item = player.inventory.itemInMainHand
        var checkTick = reloadTime.tick
        if (!check(gunStats)) {
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
                player.playSound(player.location, Sound.BLOCK_WOODEN_DOOR_CLOSE, 1f, 2f)
                cancel()
                return@createTask
            }

            if (item != player.inventory.itemInMainHand) {
                gunStats.reloading = false
                cancel()
                return@createTask
            }
            checkTick += -1
        }
        Bukkit.getScheduler().runTaskTimer(ZombieHero.plugin, task, 0,1)
    }
}
