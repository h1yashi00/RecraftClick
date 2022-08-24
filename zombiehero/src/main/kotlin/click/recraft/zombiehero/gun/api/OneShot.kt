package click.recraft.zombiehero.gun.api

import click.recraft.zombiehero.gun.base.Tick
import org.bukkit.Particle

class OneShot(
    override val rate:    Tick,
    override val damage:  Int,
    override val knockBack: Double,
    override val spread:  Double,
    override val recoilY: Float,
    override val recoilZ: Float,
    override val particle: Particle = Particle.ASH,
) : Shot {
    override val shootAmmo: Int = 1
}
