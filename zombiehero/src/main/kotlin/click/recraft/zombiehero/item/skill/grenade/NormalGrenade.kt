package click.recraft.zombiehero.item.skill.grenade

import click.recraft.zombiehero.gun.api.Tick
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.entity.Item
import org.bukkit.entity.Player

class NormalGrenade : Grenade(
    "普通のグレネード",
    Tick.sec(1.5),
    202,
    useDelayTick = Tick.sec(1.0),
    99999,
    10,
    arrayListOf(
        "${ChatColor.WHITE}設置したガラスを破壊することができる",
        "${ChatColor.WHITE}爆発周囲にダメージを与える",
    )
) {
    override fun pickUp(player: Player, item: Item) {
    }

    override fun explosion(entity: Item, location: Location) {
        val loc = entity.location.clone()
        loc.world!!.spawnParticle(Particle.EXPLOSION_HUGE, loc, 1)
        loc.world!!.createExplosion(
            loc,
            10f,
            false,
            true,
            entity
        )
    }
}