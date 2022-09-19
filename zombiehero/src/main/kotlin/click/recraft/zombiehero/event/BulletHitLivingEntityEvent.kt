package click.recraft.zombiehero.event

import org.bukkit.Location
import org.bukkit.entity.LivingEntity
import org.bukkit.event.Cancellable
import org.bukkit.event.Event
import org.bukkit.event.HandlerList
import org.bukkit.util.BoundingBox

class BulletHitLivingEntityEvent (
    var shooter: LivingEntity,
    var bulletLocation: Location,
    var bulletBoundingBox: BoundingBox,
    var isHeadShot: Boolean,
    var hitLivingEntity: LivingEntity,
    var knockBack: Double,
    var damage: Double
): Event(), Cancellable{
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

    var canceller = false

    override fun isCancelled(): Boolean {
        return canceller
    }

    override fun setCancelled(cancel: Boolean) {
        canceller = cancel
    }
}