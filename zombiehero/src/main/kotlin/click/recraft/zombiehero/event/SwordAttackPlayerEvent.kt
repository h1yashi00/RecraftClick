package click.recraft.zombiehero.event

import click.recraft.zombiehero.item.melee.Sword
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.Cancellable
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class SwordAttackPlayerEvent(
    val source: Player,
    val sword: Sword,
    var damage: Double,
    val isHeadShot: Boolean,
    val victim: LivingEntity
): Event(), Cancellable {
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