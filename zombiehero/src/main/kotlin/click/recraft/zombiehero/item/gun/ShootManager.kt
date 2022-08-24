package click.recraft.zombiehero.item.gun

import click.recraft.zombiehero.Util
import click.recraft.zombiehero.ZombieHero
import click.recraft.zombiehero.item.PlayerGun
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.*
import kotlin.collections.HashMap

class ShootManager {
    private val manager: HashMap<UUID, Data> = hashMapOf()
    private data class Data(val gun: PlayerGun, val tick: Long = System.currentTimeMillis())
    init {
        val task = Util.createTask {
            manager.iterator().forEach {
                val uuid = it.key
                val data = it.value
                val player = Bukkit.getPlayer(uuid) ?: return@forEach
                val item = player.inventory.itemInMainHand
                if (data.gun.itemStack != item) {
                    manager.remove(player.uniqueId)
                    return@forEach
                }
                val passed = System.currentTimeMillis() - data.tick
                if (passed <= 10) {
                    manager[player.uniqueId] = Data(data.gun)
                }
                if (passed >= 200) {
                    manager.remove(player.uniqueId)
                    return@forEach
                }
                // 処理をすることろ
                data.gun.shootAction(player)
                }
            }
            Bukkit.getScheduler().runTaskTimer(ZombieHero.plugin, task, 0,1)
        }
    fun register(player: Player, playerGun: PlayerGun) {
        manager[player.uniqueId] = Data(playerGun)
    }
    fun remove(player: Player) {
        manager.remove(player.uniqueId)
    }
}
