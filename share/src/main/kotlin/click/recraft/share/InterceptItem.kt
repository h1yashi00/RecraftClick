package click.recraft.share

import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack

data class PlayerInterceptAction(val player: Player)

fun PlayerInterceptAction.action() {
}

fun KotlinPlugin.interactItem(
    item: ItemStack,
    rightClick: Boolean,
    leftClick: Boolean,
    function: PlayerInterceptAction.() -> Unit,
): InteractItemDSL {
    val dsl = InteractItemDSL(this, item, rightClick, leftClick, function)
    interceptItemsDSLs.add(dsl)
    return dsl
}

class InteractItemDSL(
    private val plugin: KotlinPlugin,
    private val item: ItemStack,
    private val rightClick: Boolean,
    private val leftClick: Boolean,
    private val function: PlayerInterceptAction.() -> Unit,
) {
    fun getItem(): ItemStack {
        return item
    }
    fun load() {
        val listener = object: Listener {
            @EventHandler
            fun drop(event: PlayerDropItemEvent) {
                val item = event.itemDrop.itemStack
                val meta = item.itemMeta ?: return
                if (meta != item.itemMeta) return
                event.isCancelled = true
            }

            @EventHandler
            fun interact(event: PlayerInteractEvent) {
                val passAction = arrayListOf<Action>()
                val player = event.player
                if (rightClick) {passAction.add(Action.RIGHT_CLICK_AIR); passAction.add(Action.RIGHT_CLICK_BLOCK)}
                if (leftClick)  {passAction.add(Action.LEFT_CLICK_AIR); passAction.add(Action.LEFT_CLICK_BLOCK)}
                if (!passAction.contains(event.action)) return
                val currentItem = event.item ?: return
                val meta = currentItem.itemMeta ?: return
                if (meta != item.itemMeta) return

                event.isCancelled = true
                val action = PlayerInterceptAction(player)
                function.invoke(action)
            }
        }
        val pm = plugin.server.pluginManager
        pm.registerEvents(listener, plugin)
    }
}