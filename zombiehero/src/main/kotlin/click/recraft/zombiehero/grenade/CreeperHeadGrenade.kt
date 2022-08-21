package click.recraft.zombiehero.grenade

import click.recraft.zombiehero.Grenade
import click.recraft.zombiehero.Util
import org.bukkit.Material
import org.bukkit.Sound

class CreeperHeadGrenade: Grenade(
    Material.CREEPER_HEAD,
    42342,
    "CreeperHead",
    20*3,
    { loc ->
        val entities = loc.world!!.getNearbyEntities(Util.makeBoundingBox(loc, 5.0))
        loc.world!!.playSound(loc, Sound.ENTITY_GENERIC_EXPLODE, 0.5f,0.5f)
        entities.forEach {
            val vec = it.location.apply{y+=1}.toVector().subtract(loc.toVector())
            if (vec.x == 0.0 && vec.y == 0.0 && vec.z == 0.0) return@forEach
            it.velocity = vec.normalize().multiply(1.8)
        }
    }
) {
}