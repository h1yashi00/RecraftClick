package click.recraft.zombiehero.event

import click.recraft.zombiehero.item.skill.grenade.Grenade
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.event.Cancellable
import org.bukkit.event.Event
import org.bukkit.event.HandlerList
import java.util.*
import kotlin.collections.HashMap

class GrenadeExplodeEvent(
    val player: Player,
    val grenade: Grenade,
    val damagedEntity: HashMap<UUID, Double>,
    val blockList: MutableList<Block>,
) : Event(), Cancellable {
    companion object {
        private val HANDLERS = HandlerList()
        @JvmStatic
        fun getHandlerList(): HandlerList {
            return HANDLERS
        }
    }

    override fun getHandlers(): HandlerList {
        return HANDLERS
    }

    var cancel = false
    override fun isCancelled(): Boolean {
        return cancel
    }

    override fun setCancelled(cancel: Boolean) {
        this.cancel = cancel
    }
}