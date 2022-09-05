package click.recraft.zombiehero.gun.api

import click.recraft.zombiehero.item.gun.Gun
import org.bukkit.Particle
import org.bukkit.entity.Player
import java.util.*
import kotlin.collections.HashMap

class MultiShot (
    override val rate: Tick,
    override val damage: Int,
    override val shootAmmo: Int,
    override val knockBack: Double,
    override val accuracy: Accuracy,
    override val knockBackAccumulateTime: Tick,
    override val particle: Particle = Particle.ASH,
) : Shot {
    override val saveKnockBack: HashMap<UUID, Double> = hashMapOf()

    override fun shootAction(player: Player, playerGun: Gun) {
        playerGun.cancelReload()
        return super.shootAction(player, playerGun)
    }
    override var lastShot: Int = 0
}