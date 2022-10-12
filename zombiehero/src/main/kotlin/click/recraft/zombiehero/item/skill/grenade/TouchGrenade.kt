package click.recraft.zombiehero.item.skill.grenade

import click.recraft.zombiehero.gun.api.Tick
import org.bukkit.Location
import org.bukkit.entity.Item
import org.bukkit.entity.Player

abstract class TouchGrenade (
    name: String,
    explosionDelay: Tick,
    customModelData: Int,
    useDelayTick: Tick,
    pickUpDelay: Int,
    description: ArrayList<String>
): Grenade(
    name,
    explosionDelay,
    customModelData = customModelData,
    useDelayTick = useDelayTick,
    pickUpDelay,
    10,
    description
) {
    var touched = false
    override fun pickUp(player: Player, item: Item) {
        explosion(item, item.location)
        touched = true
        item.remove()
    }


    override fun launchGrenade(player: Player, location: Location) {
        touched = false
        super.launchGrenade(player, location)
    }
}