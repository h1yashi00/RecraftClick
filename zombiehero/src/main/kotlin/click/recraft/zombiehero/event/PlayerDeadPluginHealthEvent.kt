package click.recraft.zombiehero.event

import click.recraft.zombiehero.item.CustomItem
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class PlayerDeadPluginHealthEvent (
    val victim: Player,
    val attacker: LivingEntity,
    val customItem: CustomItem?,
    val isHeadShot: Boolean
): Event() {
    var reviveHp = 1000
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
}