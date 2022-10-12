package click.recraft.zombiehero.item.skill.grenade

import click.recraft.zombiehero.Util
import click.recraft.zombiehero.ZombieHero
import click.recraft.zombiehero.gun.api.Tick
import click.recraft.zombiehero.item.skill.SkillItem
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Item
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.player.PlayerInteractEvent

abstract class Grenade (
    val name: String,
    private val explosionDelay: Tick,
    customModelData: Int,
    private val useDelayTick: Tick,
    private val pickUpDelay: Int,
    val damageMultiplier: Int,
    description: ArrayList<String>
)
: SkillItem (
    Material.ORANGE_DYE,
    description = description,
    displayName = "${ChatColor.GOLD}$name",
    customModelData = customModelData,
) {
    private var useDelay = ZombieHero.plugin.getTime()
    abstract fun pickUp(player: Player, item: Item)
    abstract fun explosion(entity: Item, location: Location)

    fun getRemainingTime(): String {
        val remainTime = (useDelay - ZombieHero.plugin.getTime()) * 0.05
        if (remainTime <= 0) {
            return "ready"
        }
        return "%.1f".format(remainTime)
    }

    open fun launchGrenade(player: Player, location: Location) {
        val dir = location.direction.clone()
        val item = createItemStack()
        val world = location.world!!
        val entity = world.dropItem(location, item)
        entity.owner = player.uniqueId
        entity.velocity = dir.multiply(1)
        entity.pickupDelay = pickUpDelay
        val task = Util.createTask {
            explosion(entity, location)
            entity.remove()
        }
        Bukkit.getScheduler().runTaskLater(ZombieHero.plugin, task, explosionDelay.tick.toLong())
    }

    override fun inInvItemClick(clickType: ClickType, player: Player) {
    }

    override fun rightClick(event: PlayerInteractEvent) {
        event.isCancelled = true
        val player = event.player
        val passedTime = ZombieHero.plugin.getTime() - useDelay
        if (passedTime > 0) {
            launchGrenade(player, player.eyeLocation)
            useDelay = ZombieHero.plugin.getTime() + useDelayTick.tick
        }
    }

    override fun leftClick(event: PlayerInteractEvent) {
    }
}