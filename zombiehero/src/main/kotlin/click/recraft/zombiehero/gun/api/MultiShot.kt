package click.recraft.zombiehero.gun.api

import click.recraft.zombiehero.gun.base.Tick
import org.bukkit.Particle

class MultiShot (
    override var rate: Tick = Tick.sec(0.0),
    override var damage: Int = 0,
    override var shootAmmo: Int = 0,
    override var knockBack: Double = 0.0,
    override var spread: Double = 0.0,
    override var recoilY: Float = 0F,
    override var recoilZ: Float = 0F,
    override var particle: Particle = Particle.ASH,
) : Shot {

}