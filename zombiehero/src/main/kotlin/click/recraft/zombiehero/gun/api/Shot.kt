package click.recraft.zombiehero.gun.api

import click.recraft.zombiehero.Util
import click.recraft.zombiehero.gun.base.Tick
import click.recraft.zombiehero.item.PlayerGun
import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.block.Block
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.entity.Zombie
import org.bukkit.util.Vector
import java.util.*

interface Shot {
    val knockBackManager: KnockBackManager
    val knockBackAccumulateTime: Tick
    private fun bulletHitBlockEffect(block: Block) {
        val loc = block.location.clone()
        loc.apply { x += 0.5; y += 0.5; z+=0.5 }
        loc.world!!.spawnParticle(Particle.BLOCK_CRACK, loc, 100, block.type.createBlockData())
    }

    private fun showBallistic(location: Location) {
        val loc = location.clone()
        loc.world!!.spawnParticle(particle, loc, 1)
    }

    private fun rand(spread: Double): Double {
        val rangeMin = -spread
        val rangeMax =  spread
        return rangeMin + (rangeMax - rangeMin) * Random().nextDouble()
    }

    private fun forceRecoil(player: Player) {
        val pitch = player.location.pitch
        val yaw = player.location.yaw
        if (recoilY == 0F && recoilZ == 0F) return
        player.setRotation(yaw - recoilZ,pitch - recoilY)
    }

    fun bulletRange(): Int {
        return 100
    }

    private fun shootArmo(bullet: Location, player: Player) {
        bullet.apply { direction = Vector(direction.x + rand(spread), direction.y + rand(spread), direction.z + rand(spread)) }
        (0..(bulletRange() * 2)).forEach{ count ->
            showBallistic(bullet)
            bullet.apply { x += (direction.x / 2) ; y += (direction.y / 2); z += (direction.z /2)}
            if (bullet.block.type.isSolid) {
                bulletHitBlockEffect(bullet.block)
                return
            }
            val bulletBoundingBox = Util.makeBoundingBox(bullet,0.1)
            val entities = bullet.world!!.getNearbyEntities(bulletBoundingBox)
            if (entities.isNotEmpty()) {
                if (entities.contains(player)) { return@forEach }
                val entity = entities.first()
                if (entity.isDead) { return@forEach }
                if (entity is ArmorStand) return@forEach
                // コンパイルくんに知らせるためのやつ
                if (entity !is LivingEntity) return@forEach
                val dmg = if (Util.isHeadLocation(bulletBoundingBox, entity.eyeLocation)) {
                    damage * 2
                }
                else {
                    damage
                }
                entity.damage(dmg.toDouble())
                if (entity is Zombie) {
                    entity.target = player
                }
                val dir = player.location.direction.clone()
                knockBackManager.register(entity, dir, knockBack, knockBackAccumulateTime.getMilliseconds().toLong())
                entity.noDamageTicks = 0
                return
            }
        }
    }

    fun shoot(player: Player, stats: PlayerGun.GunStats) {
        forceRecoil(player)
        player.world.playSound(player.location, Sound.ENTITY_IRON_GOLEM_HURT, 1f, 3F)
        val dir = player.location.direction
        player.location.direction = dir.multiply(1)
        val bullet = player.location.clone().apply {y+=player.eyeHeight }
        (1..shootAmmo).forEach { _ ->
            shootArmo(bullet.clone(), player)
        }
    }

    var lastShot: Long

    val rate:   Tick
    val damage: Int
    val knockBack: Double
    val shootAmmo: Int
    val spread: Double
    val recoilY: Float
    val recoilZ: Float
    val particle: Particle
}
