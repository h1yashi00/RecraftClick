package click.recraft.zombiehero.gun.api

import click.recraft.zombiehero.Util
import click.recraft.zombiehero.ZombieHero
import click.recraft.zombiehero.item.gun.Gun
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.util.*

class ReloadManager {
    private val save = hashMapOf<UUID, Data>()
    private data class Data(
        val gun: Gun,
        var tick: Int,
        val reloadTime: Int,
        val pastItem: ItemStack,
        val uuid: UUID
    )
    init {
        val task = Util.createTask {
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
                    gunStats.reloading = false
                    player.sendExperienceChange(1f, gunStats.totalArmo)
                    val reloadItem = player.inventory.itemInMainHand.clone()
                        .apply {
                            amount = gunStats.currentArmo
                        }
                    player.inventory.setItemInMainHand(reloadItem)
                    player.playSound(player.location, Sound.BLOCK_WOODEN_DOOR_CLOSE, 1f, 2f)
                    save.remove(uuid)
                    return@forEach
                }

                if (item != player.inventory.itemInMainHand) {
                    gunStats.reloading = false
                    save.remove(uuid)
                    return@forEach
                }
                data.tick += -1
            }
        }
        Bukkit.getScheduler().runTaskTimer(ZombieHero.plugin, task, 10,1)
    }

    fun register(gun: Gun, player: Player) {
        save[gun.unique] = Data(gun,  gun.getReloadTime(), gun.getReloadTime(), player.inventory.itemInMainHand,  player.uniqueId)
    }
}