package click.recraft.zombiehero.event

import click.recraft.zombiehero.item.CustomItem
import org.bukkit.entity.LivingEntity
import org.bukkit.event.Cancellable
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class PluginHealthDamageEvent(
    val damager: LivingEntity,
    val victim: LivingEntity,
    var damage: Int,
    val sourceCustomItem: CustomItem?,
    val isHeadShot: Boolean,
): Event(), Cancellable {
    companion object {
        val HANDLERS = HandlerList()
        @JvmStatic
        fun getHandlerList(): HandlerList {
            return HANDLERS
        }
    }
    override fun getHandlers(): HandlerList {
        return HANDLERS
    }

    private var cancel = false
    override fun isCancelled(): Boolean {
        return cancel
    }

    override fun setCancelled(cancel: Boolean) {
        this.cancel = cancel
    }
}