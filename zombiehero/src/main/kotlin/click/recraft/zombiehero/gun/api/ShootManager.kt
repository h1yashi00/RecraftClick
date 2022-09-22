package click.recraft.zombiehero.gun.api

import click.recraft.zombiehero.Util
import click.recraft.zombiehero.ZombieHero
import click.recraft.zombiehero.item.gun.Gun
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.*
import kotlin.collections.HashMap

class ShootManager {
    private val manager: HashMap<UUID, Data> = hashMapOf()
    private data class Data(val playerUUID: UUID, val gun: Gun, var passedTick: Int = 5, val time: Int = ZombieHero.plugin.getTime())
    init {
        val task = Util.createTask {
                // 処理をすることろ
                // GunSatsの影響で球が打てなかった場合に､タスクをなくすうようにする
            ZombieHero.plugin.importantTaskId.add(taskId)
            manager.iterator().forEach {
                val data = it.value
                val player = Bukkit.getPlayer(data.playerUUID) ?: return@forEach
                val item = player.inventory.itemInMainHand
                if (!data.gun.isSimilar(item)) {
                    manager.remove(data.gun.unique)
                    return@forEach
                }
                // 処理をすることろ
                // GunStatsの影響で球が打てなかった場合に､タスクをなくすうようにする
                data.gun.shootAction(player)
                if (data.passedTick <= 0) {
                    manager.remove(data.gun.unique)
                    return@forEach
                }
                data.passedTick += -1
            }
        }
        Bukkit.getScheduler().runTaskTimer(ZombieHero.plugin, task, 0,1)
    }
    fun register(player: Player, gun: Gun) {
        val data = manager[gun.unique]
        if (data == null) {
            manager[gun.unique] =  Data(player.uniqueId, gun)
            return
        }
        if (data.time == ZombieHero.plugin.getTime()) {
            return
        }
        else {
            manager[gun.unique] =  Data(player.uniqueId, gun)
        }
    }
}
