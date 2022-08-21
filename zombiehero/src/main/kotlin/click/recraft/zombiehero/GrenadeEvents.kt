package click.recraft.zombiehero

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.player.PlayerInteractEvent

class GrenadeEvents: Listener {
    @EventHandler
    fun place(event: BlockPlaceEvent) {
        val player = event.player
        val holdingItem = player.inventory.itemInMainHand
        val loc = player.eyeLocation.clone()
        ZombieHero.plugin.grenades.forEach { grenadeBase ->
            val grenade = grenadeBase.isItem(holdingItem) ?: return@forEach
            event.isCancelled = true
            grenade.action(loc)
        }
    }
    @EventHandler
    fun rightClick(event: PlayerInteractEvent) {
        if (!(event.action == Action.RIGHT_CLICK_BLOCK || event.action == Action.RIGHT_CLICK_AIR)) return
        val player = event.player
        val holdingItem = player.inventory.itemInMainHand
        val loc = player.eyeLocation.clone()
        ZombieHero.plugin.grenades.forEach { grenadeBase ->
            val grenade = grenadeBase.isItem(holdingItem) ?: return@forEach
            grenade.action(loc)
        }
    }
}