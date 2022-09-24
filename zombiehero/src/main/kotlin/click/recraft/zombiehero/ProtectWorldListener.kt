package click.recraft.zombiehero

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockFromToEvent

class ProtectWorldListener: Listener {
    @EventHandler
    fun flow(event: BlockFromToEvent) {
        event.isCancelled = true
    }
}
