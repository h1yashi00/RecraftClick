package click.recraft.zombiehero.item.grenade

import click.recraft.zombiehero.ZombieHero
import click.recraft.zombiehero.item.CustomItemListener
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityPickupItemEvent

class GrenadeListener: CustomItemListener (
    ZombieHero.plugin.customItemFactory
) {
    @EventHandler
    fun pickUp(event: EntityPickupItemEvent) {
        val player = if (event.entity is Player) {event.entity as Player} else {return}
        val customItem = getItem(event.item.itemStack) ?: return
        val grenade = if (customItem is Grenade) {customItem}  else return
        event.isCancelled = true
        grenade.pickUp(player, event.item)
    }
}