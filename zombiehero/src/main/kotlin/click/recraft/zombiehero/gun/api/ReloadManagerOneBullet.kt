package click.recraft.zombiehero.gun.api

import click.recraft.zombiehero.Util
import click.recraft.zombiehero.ZombieHero
import org.bukkit.Bukkit

class ReloadManagerOneBullet: ReloadManager() {
    init {
        val task = Util.createTask {
            save.iterator().forEach {(uuid, data) ->
                val gun = data.gun
                val gunStats = gun.stats
                val player = Bukkit.getPlayer(data.uuid)
                if (player == null) {
                    save.remove(uuid)
                    return@forEach
                }
                data.tick -= 1
                player.sendExperienceChange(data.tick.toFloat() / data.reloadTime , gunStats.totalArmo)
                if (data.tick > 0) {
                    return@forEach
                }
                player.sendExperienceChange(1f , gunStats.totalArmo)
                gunStats.totalArmo += -1
                gunStats.currentArmo += 1
                save.remove(uuid)
                if (gunStats.currentArmo == gun.stats.maxArmo) {
                    val gunSound = gun.reloadFinishSound
                    player.playSound(player.location, gunSound.type.sound, gunSound.volume,gunSound.pitch)
                }
                gun.reload(player)
            }
        }
        Bukkit.getScheduler().runTaskTimer(ZombieHero.plugin, task, 10,1)
    }
}