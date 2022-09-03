package click.recraft.zombiehero.item.gun

import click.recraft.zombiehero.item.CustomItemListener
import click.recraft.zombiehero.item.CustomItemManager
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerItemHeldEvent

open class GunListener(
    manager: CustomItemManager
) : CustomItemListener(
    manager
) {
    @EventHandler
    fun switchItem(event: PlayerItemHeldEvent) {
        val player = event.player
        val item = player.inventory.getItem(event.previousSlot)
        val customItem = getItem(item) ?: return
        if (customItem !is Gun) { return }
        if (customItem.isScoping()) {
            customItem.cancelScope(player)
        }
        if (customItem.isRelaoding()) {
            customItem.cancelReload()
        }
    }
    @EventHandler
    fun swap(event: PlayerItemHeldEvent) {
        val player = event.player
        val item = player.inventory.getItem(event.newSlot) ?: return
        if (item.type.isAir) {
            return
        }
        val customItem = getItem(item) ?: return
        if (customItem !is Gun) return
        customItem.playerGiveItem(player)
    }
    @EventHandler
    override fun itemDrop(e: PlayerDropItemEvent) {
        val player = e.player
        val item = getItem(e.itemDrop.itemStack) ?: return
        if (!item.isDroppable()) {
            e.isCancelled = true
        }
        (item as Gun).reload(player)
        item.isQDrop()
    }
}