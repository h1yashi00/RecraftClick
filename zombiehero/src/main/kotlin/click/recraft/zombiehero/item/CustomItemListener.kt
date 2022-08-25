package click.recraft.zombiehero.item

import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerSwapHandItemsEvent
import org.bukkit.inventory.ItemStack

interface CustomItemListener: Listener {
    val manager: CustomItemManager

    fun getItem(itemStack: ItemStack?): CustomItem? {
        return manager.getItem(itemStack)
    }

    @EventHandler
    fun itemDrop(e: PlayerDropItemEvent) {
        val item = getItem(e.itemDrop.itemStack) ?: return
        if (!item.isDroppable()) {
            e.isCancelled = true
        }
    }

    @EventHandler
    fun invClick(e: InventoryClickEvent) {
        val player = e.whoClicked as Player
        val currentItem = if (e.click == ClickType.NUMBER_KEY) {
            // NumberKeyの場合はプレイヤーのインベントリのタイムが移動するのでプレイヤーのインベントリから取り出す
            player.inventory.getItem(e.hotbarButton)
        } else {
            e.currentItem
        }
        val item= getItem(currentItem) ?: return
        e.isCancelled = true
        if (e.currentItem == null || e.currentItem!!.type == Material.AIR) {
            return
        }
        e.isCancelled = !item.isMovable()
        item.inInvItemClick(e.click, e.whoClicked as Player)
    }

    @EventHandler
    fun onItemSwap(e: PlayerSwapHandItemsEvent) {
        val player = e.player
        val mainHandItem = player.inventory.itemInMainHand
        if (mainHandItem.type.isAir) {
            return
        }
        val mainHandCustomItem = getItem(mainHandItem) ?: return
        e.isCancelled =  !mainHandCustomItem.isSwapable()
    }

    @EventHandler
    fun click(e: PlayerInteractEvent) {
        val player = e.player
        val itemStack = player.inventory.itemInMainHand
        val item = getItem(itemStack) ?: return
        item.itemInteract(e, e.hand)
    }
}