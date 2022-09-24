package click.recraft.zombiehero.item

import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerItemHeldEvent
import org.bukkit.event.player.PlayerSwapHandItemsEvent
import org.bukkit.inventory.ItemStack

abstract class CustomItemListener (
    private val manager: CustomItemManager
): Listener {

    fun getItem(itemStack: ItemStack?): CustomItem? {
        return manager.getItem(itemStack)
    }

    @EventHandler
    open fun itemDrop(e: PlayerDropItemEvent) {
        val item = getItem(e.itemDrop.itemStack) ?: return
        if (!item.isDroppable()) {
            e.isCancelled = true
        }
    }

    @EventHandler
    fun invClick(e: InventoryClickEvent) {
        val player = e.whoClicked as Player
        val currentItem = if (e.click == ClickType.NUMBER_KEY) {
            val selectedItem = if (e.clickedInventory!!.type == InventoryType.PLAYER) {
                e.slot
            }
            else {
                e.hotbarButton
            }
            player.inventory.getItem(selectedItem)
            // NumberKeyの場合はプレイヤーのインベントリのタイムが移動するのでプレイヤーのインベントリから取り出す
        } else {
            e.currentItem
        }
        var item= getItem(currentItem)
        if (item == null) {
            if (e.hotbarButton == -1) return
            item = getItem(player.inventory.getItem(e.hotbarButton)) ?: return
        }
        e.isCancelled = !item.isMovable()
        item.inInvItemClick(e.click, e.whoClicked as Player)
    }

    @EventHandler
    fun changeItem(event: PlayerItemHeldEvent) {
        val player = event.player
        val item = getItem(player.inventory.getItem(event.newSlot))
        item?.currentItem(event)
        val prevItem = getItem(player.inventory.getItem(event.previousSlot))
        prevItem?.prevItem(event)
    }

    @EventHandler
    fun onItemSwapMainHand(e: PlayerSwapHandItemsEvent) {
        val player = e.player
        val mainHandItem = player.inventory.itemInMainHand
        if (mainHandItem.type.isAir) {
            return
        }
        val mainHandCustomItem = getItem(mainHandItem) ?: return
        e.isCancelled =  !mainHandCustomItem.isSwapable()
    }
    @EventHandler
    fun onItemSwapOffHand(e: PlayerSwapHandItemsEvent) {
        val player = e.player
        val offHand = player.inventory.itemInOffHand
        if (offHand.type.isAir) {
            return
        }
        val mainHandCustomItem = getItem(offHand) ?: return
        e.isCancelled =  !mainHandCustomItem.isSwapable()
    }

    @EventHandler
    fun click(e: PlayerInteractEvent) {
        val player = e.player
        val itemStack = player.inventory.itemInMainHand
        val item = getItem(itemStack) ?: return
        val action = e.action
        if (action == Action.LEFT_CLICK_AIR   || action == Action.LEFT_CLICK_BLOCK) {
            item.leftClick(e)
        }
        if (action == Action.RIGHT_CLICK_AIR  || action == Action.RIGHT_CLICK_BLOCK) {
            item.rightClick(e)
        }
    }
}