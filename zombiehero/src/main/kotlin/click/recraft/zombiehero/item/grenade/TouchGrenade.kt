package click.recraft.zombiehero.item.grenade

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
): Grenade (
    name,
    explosionDelay,
    customModelData = customModelData,
    useDelayTick = useDelayTick,
    pickUpDelay
) {
    var touched = false
    override fun pickUp(player: Player, item: Item) {
        explosion(item.location)
        touched = true
        item.remove()
    }


    override fun launchGrenade(location: Location) {
        touched = false
        super.launchGrenade(location)
    }
}