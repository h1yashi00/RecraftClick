package click.recraft.zombiehero.gun.api

import click.recraft.zombiehero.Util
import click.recraft.zombiehero.ZombieHero
import click.recraft.zombiehero.item.gun.PlayerGun
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.util.*

class ReloadManager {
    private val save = hashMapOf<UUID, Data>()
    private data class Data(
        val gun: PlayerGun,
        val player: Player,
        var tick: Int,
        val pastItem: ItemStack,
    )
    init {
        val task = Util.createTask {
            save.iterator().forEach {(uuid, data) ->
                val gun = data.gun
                val gunStats = gun.stats
                val player = data.player
                if (!gunStats.reloading) {
                    save.remove(uuid)
                    return@forEach
                }
                if (player.inventory.itemInMainHand != data.pastItem) {
                    gunStats.reloading = false
                    save.remove(uuid)
                    return@forEach
                }
                data.tick -= 1
                player.sendExperienceChange(data.tick.toFloat() / data.gun.reload.reloadTime.tick , gunStats.totalArmo)
                if (data.tick > 0) {
                    return@forEach
                }
                player.sendExperienceChange(1f , gunStats.totalArmo)
                gunStats.reloading = false
                gunStats.totalArmo += -1
                gunStats.currentArmo += 1
                save.remove(uuid)
                val reloadItem = player.inventory.itemInMainHand.clone()
                    .apply {
                        amount = gunStats.currentArmo
                    }
                player.inventory.setItemInMainHand(reloadItem)
                if (gunStats.currentArmo == gun.reload.armo) {
                    player.playSound(player.location, Sound.BLOCK_WOODEN_DOOR_CLOSE,1f, 2f)
                }
                gun.reload(player)
            }
        }
        Bukkit.getScheduler().runTaskTimer(ZombieHero.plugin, task, 10,1)
    }

    fun register(gun: PlayerGun, player: Player) {
        save[gun.uuid] = Data(gun, player, gun.reload.reloadTime.tick, player.inventory.itemInMainHand)
    }
}