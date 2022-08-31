package click.recraft.zombiehero.item.grenade

import click.recraft.zombiehero.item.CustomItemListener
import click.recraft.zombiehero.item.CustomItemManager
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityPickupItemEvent

class TouchGrenadeListener(
    override val manager: CustomItemManager
) : CustomItemListener{
    @EventHandler
    fun pickup(event: EntityPickupItemEvent) {
        val item = event.item
        val customItem = getItem(item.itemStack) ?: return
        if (event.entity !is Player) {return}
        event.isCancelled = true
        event.item.customName = "removed"
        item.remove()
        (customItem as TouchGrenade).explode(item.location)
    }
}
