package click.recraft.zombiehero.gun.api

import click.recraft.share.extension.runTaskTimer
import click.recraft.zombiehero.ZombieHero
import org.bukkit.Bukkit

class ReloadManagerFullBullet: ReloadManager() {
    init {
        runTaskTimer(10, 1) {
            ZombieHero.plugin.importantTaskId.add(taskId)
            save.iterator().forEach {(uuid, data) ->
                val player = Bukkit.getPlayer(data.uuid)

                if (player == null) {
                    save.remove(uuid)
                    return@forEach
                }

                val gun = data.gun
                val gunStats = gun.stats
                val item = player.inventory.itemInMainHand
                val checkTick = data.tick

                player.sendExperienceChange(checkTick.toFloat() / data.reloadTime.toFloat(), gunStats.totalArmo)

                if (checkTick <= 0) {
                    val shootArmo = gunStats.maxArmo - gunStats.currentArmo
                    val restoreArmo = if (shootArmo <= gunStats.totalArmo) {
                        shootArmo
                    }
                    else {
                        gunStats.totalArmo
                    }
                    gunStats.totalArmo   +=  -restoreArmo
                    gunStats.currentArmo +=   restoreArmo
                    player.sendExperienceChange(1f, gunStats.totalArmo)
                    val gunSound = gun.reloadFinishSound
                    player.playSound(player.location, gunSound.type.sound, gunSound.volume,gunSound.pitch)
                    save.remove(uuid)
                    return@forEach
                }

                if (item != player.inventory.itemInMainHand) {
                    save.remove(uuid)
                    return@forEach
                }
                data.tick += -1
            }
        }
    }
}