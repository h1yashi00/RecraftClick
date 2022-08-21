package click.recraft.zombiehero

import org.bukkit.Location
import org.bukkit.Material

open class Grenade (
    override val material: Material,
    override val customModelValue: Int,
    override val name: String,
    override val explosionDelay: Int,
    override val explosionFunction: (Location) -> Unit,
) : GrenadeBase {
}