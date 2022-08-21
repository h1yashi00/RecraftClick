package click.recraft.zombiehero

import click.recraft.zombiehero.gun.base.RightClickAction
import org.bukkit.*
import org.bukkit.block.BlockFace
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitTask
import java.util.*

class HoldKeyEvent: Listener {
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
                ZombieHero.plugin.guns.forEach { gun ->
                    if (gun.isGun(item) != null) {
                        gun.shoot(player)
                    }
                }
            }
        }
        Bukkit.getScheduler().runTaskTimer(ZombieHero.plugin, task, 0,1)
    }

    @EventHandler
    fun gunReloadQDrop(event: PlayerDropItemEvent) {
        ZombieHero.plugin.guns.forEach {
            if (it.isGun(event.itemDrop.itemStack) != null) {
                event.isCancelled = true
                val item = it.getItem(0, false)
                event.itemDrop.itemStack = item
                val player = event.player
                val task: (BukkitTask) -> Unit = {
                    Bukkit.getPluginManager().callEvent(PlayerInteractEvent(player, Action.RIGHT_CLICK_AIR, item, null, BlockFace.SELF))
                }
                Bukkit.getScheduler().runTaskLater(ZombieHero.plugin, task, 1)
            }
        }
    }

    @EventHandler
    fun gunTaskScheduleHandler(event: PlayerInteractEvent) {
        //left click action
        val player = event.player
        val item = player.inventory.itemInMainHand
        if (event.action == Action.LEFT_CLICK_BLOCK || event.action == Action.LEFT_CLICK_AIR) {
            ZombieHero.plugin.guns.forEach { gun ->
                if (gun.isGun(item) != null) {
                    if (gun is RightClickAction) {
                        gun.rightClickAction(player)
                    }
                }
            }
            return
        }
        //right click action
        if (item.type.isAir) return
        saved[player.uniqueId] = HoldKeyPlayerData(System.currentTimeMillis(), item)
    }
}