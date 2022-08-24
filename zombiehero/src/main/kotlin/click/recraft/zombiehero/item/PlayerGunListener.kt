package click.recraft.zombiehero.item

import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerDropItemEvent

class PlayerGunListener(
    override val manager: PlayerGunManager
) : CustomItemListener {
    @EventHandler
    override fun itemDrop(e: PlayerDropItemEvent) {
        val player = e.player
        val item = getItem(player.inventory.itemInMainHand) ?: return
        if (!item.isDroppable()) {
            e.isCancelled = true
        }
        (item as PlayerGun).reload(player)
    }
}