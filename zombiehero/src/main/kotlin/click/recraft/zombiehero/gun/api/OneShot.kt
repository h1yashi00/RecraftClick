package click.recraft.zombiehero.gun.api

import click.recraft.zombiehero.gun.base.Tick
import org.bukkit.Particle
import java.util.*
import kotlin.collections.HashMap

class OneShot(
    override val rate:    Tick,
    override val damage:  Int,
    override val knockBack: Double,
    override val spread:  Double,
    override val recoilY: Float,
    override val recoilZ: Float,
    override val knockBackAccumulateTime: Tick,
    override val particle: Particle = Particle.ASH,
) : Shot {
    override val saveKnockBack: HashMap<UUID, Double> = hashMapOf()
    override var lastShot: Long = System.currentTimeMillis()
    override val shootAmmo: Int = 1
}
