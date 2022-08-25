package click.recraft.zombiehero.gun.api

import click.recraft.zombiehero.gun.base.Tick
import org.bukkit.Particle

class MultiShot (
    override val rate: Tick,
    override val damage: Int,
    override val shootAmmo: Int,
    override val knockBack: Double,
    override val spread: Double,
    override val recoilY: Float,
    override val recoilZ: Float,
    override val knockBackManager: KnockBackManager,
    override val knockBackAccumulateTime: Tick,
    override val particle: Particle = Particle.ASH,
) : Shot {
    override var lastShot: Long = System.currentTimeMillis()
}