package click.recraft.zombiehero.item

import click.recraft.zombiehero.ZombieHero
import org.bukkit.Bukkit
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitTask
import java.util.*

class KeyHoldManager(
    private val gunManager: PlayerGunManager
) {
    private data class HoldKeyPlayerData(val tick: Long, val item: ItemStack)
    private val saved = hashMapOf<UUID, HoldKeyPlayerData>()
    init {
        val task: (BukkitTask) -> Unit = {
            // 時間が経過してないか確認
            // プレイヤーのアイテムを持ち替えていないか確認
            // ずっとアイテムを買えずにホールドしているプレイヤーuuid
            saved.iterator().forEach {
                val uuid = it.key
                val data = it.value
                val player = Bukkit.getPlayer(uuid) ?: return@forEach
                val item = player.inventory.itemInMainHand
                if (data.item != item) {
                    saved.remove(player.uniqueId)
                    return@forEach
                }
                val passed = System.currentTimeMillis() - data.tick
                if (passed <= 10) {
                    saved[player.uniqueId] = HoldKeyPlayerData(System.currentTimeMillis(), data.item)
                }
                if (passed >= 200) {
                    saved.remove(player.uniqueId)
                    return@forEach
                }
                // 処理をすることろ
            }
        }
        Bukkit.getScheduler().runTaskTimer(ZombieHero.plugin, task, 0,1)
    }
}
