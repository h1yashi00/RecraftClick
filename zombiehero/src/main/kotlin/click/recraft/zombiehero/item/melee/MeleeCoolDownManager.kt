package click.recraft.zombiehero.item.melee

import click.recraft.zombiehero.Util
import click.recraft.zombiehero.ZombieHero
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.util.*
import kotlin.collections.HashMap

class MeleeCoolDownManager {
    private data class Data(
        val sword: Sword,
        val item: ItemStack,
        val playerUUID: UUID,
        var remaining: Int,
        val time: Long = System.currentTimeMillis(),
    )
    private val save: HashMap<UUID, Data> = hashMapOf()
    init {
        val task = Util.createTask {
            ZombieHero.plugin.importantTaskId.add(taskId)
            save.iterator().forEach {(itemUUID, data) ->
                val player = Bukkit.getPlayer(data.playerUUID)
                if (player == null) {
                    save.remove(itemUUID)
                    return@forEach
                }
                val currentItem = player.inventory.itemInMainHand
                if (currentItem != data.item) {
                    save.remove(itemUUID)
                    return@forEach
                }
                if (data.remaining < 0) {
                    data.sword.attack(player)
                    save.remove(itemUUID)
                    return@forEach
                }
                player.sendExperienceChange(data.remaining.toFloat() / data.sword.coolDown.tick, 0)
                data.remaining += -1
            }
        }
        Bukkit.getScheduler().runTaskTimer(ZombieHero.plugin, task,10,1)
    }
    fun contains(uuid: UUID): Boolean {
        return save.contains(uuid)
    }
    fun register(sword: Sword, player: Player, pastItem: ItemStack) {
        save[sword.unique] = Data(sword, pastItem, player.uniqueId, sword.coolDown.tick)
    }
}
