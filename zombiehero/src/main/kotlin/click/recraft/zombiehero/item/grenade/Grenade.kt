package click.recraft.zombiehero.item.grenade

import click.recraft.share.item
import click.recraft.zombiehero.Util
import click.recraft.zombiehero.ZombieHero
import click.recraft.zombiehero.gun.api.Tick
import click.recraft.zombiehero.item.CustomItem
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Item
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.player.PlayerInteractEvent
import java.util.*

abstract class Grenade (
    val name: String,
    private val explosionDelay: Tick,
    customModelData: Int,
    private val useDelayTick: Tick,
    private val pickUpDelay: Int,
)
: CustomItem(
    item(
        Material.ORANGE_DYE,
        displayName = "${ChatColor.GOLD}$name",
        customModelData = customModelData,
        localizedName = UUID.randomUUID().toString(),
    ),
) {
    private var useDelay = ZombieHero.plugin.getTime()
    abstract fun pickUp(player: Player, item: Item)
    abstract fun explosion(location: Location)

    fun getRemainingTime(): String {
        val remainTime = (useDelay - ZombieHero.plugin.getTime()) * 0.05
        if (remainTime <= 0) {
            return "ready"
        }
        return "%.1f".format(remainTime)
    }

    open fun launchGrenade(location: Location) {
        val dir = location.direction.clone()
        val item = createItemStack()
        val world = location.world!!
        val entity = world.dropItem(location, item)
        entity.velocity = dir.multiply(1)
        entity.pickupDelay = pickUpDelay
        val task = Util.createTask {
            explosion(entity.location)
            entity.remove()
        }
        Bukkit.getScheduler().runTaskLater(ZombieHero.plugin, task, explosionDelay.tick.toLong())
    }

    override fun inInvItemClick(clickType: ClickType, player: Player) {
    }

    override fun rightClick(event: PlayerInteractEvent) {
        event.isCancelled = true
        val passedTime = ZombieHero.plugin.getTime() - useDelay
        if (passedTime > 0) {
            launchGrenade(event.player.eyeLocation)
            useDelay = ZombieHero.plugin.getTime() + useDelayTick.tick
        }
    }

    override fun leftClick(event: PlayerInteractEvent) {
    }
}