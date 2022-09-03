package click.recraft.zombiehero.item.grenade

import click.recraft.share.item
import click.recraft.zombiehero.ZombieHero
import click.recraft.zombiehero.gun.api.Tick
import click.recraft.zombiehero.item.CustomItem
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.scheduler.BukkitTask
import java.util.*

open class Grenade(
    name: String,
    private val explosionDelay: Tick,
    private val explosionFunction: (Location) -> Unit,
)
: CustomItem(
    item(
        Material.BLUE_DYE,
        displayName = "${ChatColor.GOLD}$name",
        customModelData = 100,
        localizedName = UUID.randomUUID().toString(),
    ),
) {
    private var useDelay = System.currentTimeMillis()

    fun action(location: Location) {
        val loc = location.clone()
        val item = loc.world!!.dropItem(loc, createItemStack()).apply {
            pickupDelay = 100000
            velocity = loc.direction.multiply(1)
        }
        var removed = false
        var count = 5
        val soundTask: (BukkitTask) -> Unit = {
            if (removed) {
                it.cancel()
            }
            if (count == 0) it.cancel()
            loc.world!!.playSound(loc, Sound.ENTITY_TNT_PRIMED, 1F,1F)
            count -= 1
        }
        Bukkit.getScheduler().runTaskTimer(ZombieHero.plugin, soundTask, 0,20)

        val task: (BukkitTask) -> Unit = {
            item.remove()
            removed = true
            explosionFunction.invoke(item.location)
        }
        Bukkit.getScheduler().runTaskLater(ZombieHero.plugin, task, explosionDelay.tick.toLong())
    }

    override fun inInvItemClick(clickType: ClickType, player: Player) {
    }

    override fun rightClick(event: PlayerInteractEvent) {
        event.isCancelled = true
        val passedTime = System.currentTimeMillis() - useDelay
        if (passedTime > 0) {
            action(event.player.eyeLocation)
            useDelay = System.currentTimeMillis() + 1000
        }
    }

    override fun leftClick(event: PlayerInteractEvent) {
    }
}