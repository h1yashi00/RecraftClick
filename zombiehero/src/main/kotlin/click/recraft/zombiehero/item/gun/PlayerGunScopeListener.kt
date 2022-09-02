package click.recraft.zombiehero.item.gun

import click.recraft.zombiehero.gun.api.GunScope
import click.recraft.zombiehero.item.CustomItemManager
import org.bukkit.event.EventHandler
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerInteractEvent

class PlayerGunScopeListener(
    private val customItemManager: CustomItemManager
): PlayerGunListener(
    customItemManager
) {
    @EventHandler
    override fun itemDrop(e: PlayerDropItemEvent) {
        super.itemDrop(e)
        val player = e.player
        val item = player.inventory.itemInMainHand
        val customItem = customItemManager.getItem(item) ?: return
        if (customItem is GunScope) {
            customItem.isQDrop()
        }
    }

    @EventHandler
    fun player(event: PlayerInteractEvent) {
        val action = event.action
        if (!(action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK)) {
            return
        }
        val player = event.player
        val item = player.inventory.itemInMainHand
        val customItem = customItemManager.getItem(item) ?: return
        if (customItem is GunScope) {
            customItem.scope(player)
        }
    }
}