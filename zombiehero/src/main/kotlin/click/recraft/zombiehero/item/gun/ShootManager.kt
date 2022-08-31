package click.recraft.zombiehero.item.gun

import click.recraft.zombiehero.Util
import click.recraft.zombiehero.ZombieHero
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.*
import kotlin.collections.HashMap

class ShootManager {
    private val manager: HashMap<UUID, Data> = hashMapOf()
    private data class Data(val uuid: UUID, val gun: Gun, val tick: Long = System.currentTimeMillis())
    init {
        val task = Util.createTask {
                // 処理をすることろ
                // GunSatsの影響で球が打てなかった場合に､タスクをなくすうようにする
            manager.iterator().forEach {
                val itemUUID = it.key
                val data = it.value
                val uuid = data.uuid
                val gun  = data.gun
                val tick = data.tick
                val player = Bukkit.getPlayer(uuid) ?: return@forEach
                val item = player.inventory.itemInMainHand
                if (!gun.isSimilar(item)) {
                    manager.remove(itemUUID)
                    return@forEach
                }
                val passed = System.currentTimeMillis() - tick
                if (passed <= 10) {
                    manager.remove(itemUUID)
                    return@forEach
                }
                if (passed >= 200) {
                    manager.remove(itemUUID)
                    return@forEach
                }
                // 処理をすることろ
                // GunStatsの影響で球が打てなかった場合に､タスクをなくすうようにする
                if (!gun.shootAction(player)) {
                    manager.remove(itemUUID)
                    return@forEach
                }
            }
        }
        Bukkit.getScheduler().runTaskTimer(ZombieHero.plugin, task, 10,1)
    }
    fun register(player: Player, playerGun: Gun) {
        manager[playerGun.unique] =  Data(player.uniqueId, playerGun)
    }
}
