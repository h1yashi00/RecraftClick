package click.recraft.zombiehero.item

import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerSwapHandItemsEvent
import org.bukkit.inventory.ItemStack
import java.util.*
import kotlin.collections.HashMap

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

    private val isCalled: HashMap<UUID, Long> = hashMapOf()
    @EventHandler
    fun click(e: PlayerInteractEvent) {
        val player = e.player
        val itemStack = player.inventory.itemInMainHand
        val item = getItem(itemStack) ?: return
        val action = e.action
        val time = isCalled[player.uniqueId]
        if (time != null) {
            if (time == System.currentTimeMillis()) {
                return
            }
        }
        isCalled[player.uniqueId] = System.currentTimeMillis()
        if (action == Action.LEFT_CLICK_AIR   || action == Action.LEFT_CLICK_BLOCK) {
            item.leftClick(e)
        }
        if (action == Action.RIGHT_CLICK_AIR  || action == Action.RIGHT_CLICK_BLOCK) {
            item.rightClick(e)
        }
    }
}