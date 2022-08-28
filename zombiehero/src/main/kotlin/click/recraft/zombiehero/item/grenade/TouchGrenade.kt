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
import org.bukkit.event.block.Action
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.scheduler.BukkitTask
import java.util.UUID

class TouchGrenade(
    touchGrenadeManager: TouchGrenadeManager,
    name: String,
    private val explosionDelay: Tick,
    private val explodeFunction: (Location) -> Unit
): CustomItem(touchGrenadeManager, item(
    Material.GRAY_DYE,
    displayName = "${ChatColor.GOLD}$name",
    localizedName = UUID.randomUUID().toString(),
    customModelData = 13432,
)) {

    private var useDelay = System.currentTimeMillis()

    fun explode(location: Location) {
        explodeFunction.invoke(location)
    }

    private fun action(location: Location) {
        val loc = location.clone()
        val item = loc.world!!.dropItem(loc, createItemStack()).apply {
            pickupDelay = 10
            velocity = loc.direction.multiply(1)
        }
        var count = 5
        val soundTask: (BukkitTask) -> Unit = {
            if (item.customName == "removed") {
                it.cancel()
            }
            if (count == 0) it.cancel()
            loc.world!!.playSound(loc, Sound.ENTITY_TNT_PRIMED, 1F,1F)
            count -= 1
        }
        Bukkit.getScheduler().runTaskTimer(ZombieHero.plugin, soundTask, 0,20)

        val task: (BukkitTask) -> Unit = {
            if (item.customName == "removed") {
                it.cancel()
            }
            else {
                explodeFunction.invoke(item.location)
            }
            item.remove()
        }
        Bukkit.getScheduler().runTaskLater(ZombieHero.plugin, task, explosionDelay.tick.toLong())
    }
    override fun inInvItemClick(clickType: ClickType, player: Player) {
    }
    override fun itemInteract(event: PlayerInteractEvent, equipmentSlot: EquipmentSlot) {
        val action = event.action
        if (action == Action.RIGHT_CLICK_AIR || event.action == Action.RIGHT_CLICK_BLOCK) {
            val passedTime = System.currentTimeMillis() - useDelay
            if (passedTime > 0) {
                action(event.player.eyeLocation)
                useDelay = System.currentTimeMillis() + 1000
            }
        }
    }
}
