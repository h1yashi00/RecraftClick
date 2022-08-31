package click.recraft.zombiehero.gun.api

import org.bukkit.Particle
import java.util.*
import kotlin.collections.HashMap

class OneShot(
    override val rate: Tick,
    override val damage:  Int,
    override val knockBack: Double,
    override val accuracy: Accuracy,
    override val knockBackAccumulateTime: Tick,
    override val particle: Particle = Particle.ASH,
) : Shot {
    override val saveKnockBack: HashMap<UUID, Double> = hashMapOf()
    override var lastShot: Long = System.currentTimeMillis()
    override val shootAmmo: Int = 1
}
