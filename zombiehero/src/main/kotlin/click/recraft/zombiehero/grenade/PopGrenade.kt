package click.recraft.zombiehero.grenade

import click.recraft.zombiehero.Grenade
import org.bukkit.Material

class PopGrenade: Grenade(Material.COCOA_BEANS, 30000, "PopGrenade", 1,{it.world!!.createExplosion(it,1F)}) {
}