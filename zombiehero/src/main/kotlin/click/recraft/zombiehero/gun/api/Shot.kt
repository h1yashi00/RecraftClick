package click.recraft.zombiehero.gun.api

import click.recraft.zombiehero.PlayerForceRecoil
import click.recraft.zombiehero.Util
import click.recraft.zombiehero.ZombieHero
import click.recraft.zombiehero.item.gun.Gun
import org.bukkit.*
import org.bukkit.block.Block
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.entity.Zombie
import org.bukkit.util.Vector
import java.util.*
import kotlin.collections.HashMap

interface Shot {
    val knockBackAccumulateTime: Tick
    val saveKnockBack: HashMap<UUID, Double>
    private fun bulletHitBlockEffect(block: Block) {
        val loc = block.location.clone()
        loc.apply { x += 0.5; y += 0.5; z+=0.5 }
        loc.world!!.spawnParticle(Particle.BLOCK_CRACK, loc, 100, block.type.createBlockData())
    }

    private fun showBallistic(location: Location) {
        val loc = location.clone()
        loc.world!!.spawnParticle(particle, loc, 1,0.0,0.0,0.0,1.0)
    }

    private fun rand(): Double {
        val rangeMin = -accuracy.value
        val rangeMax =  accuracy.value
        return rangeMin + (rangeMax - rangeMin) * Random().nextDouble()
    }

    fun bulletRange(): Int {
        return 100
    }

    private fun shootArmo(bullet: Location, player: Player) {
        bullet.apply { direction = Vector(direction.x + rand(), direction.y + rand(), direction.z + rand()) }
        (0..(bulletRange() * 2)).forEach{ _ ->
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
                entity.noDamageTicks = 0
                entity.world.spawnParticle(Particle.BLOCK_CRACK, bullet, 10, Material.REDSTONE_BLOCK.createBlockData())
                if (saveKnockBack.containsKey(entity.uniqueId)) {
                    val data = saveKnockBack[entity.uniqueId]!!
                    val newValue = data + knockBack
                    saveKnockBack[entity.uniqueId] = newValue
                }
                else {
                    saveKnockBack[entity.uniqueId] = knockBack
                    val lateTask = Util.createTask {
                        val accumulateKnockBack = saveKnockBack[entity.uniqueId]!!
                        entity.velocity = dir.multiply(accumulateKnockBack).apply { y = if (y < 0) {y * -1} else {y} }
                        saveKnockBack.remove(entity.uniqueId)
                    }
                    Bukkit.getScheduler().runTaskLater(ZombieHero.plugin, lateTask, 1)
                }
                return
            }
        }
    }

    fun shootAction(player: Player, gun: Gun) {
        if (gun.isRelaoding()) {
            gun.cancelReload()
            return
        }
        val stats = gun.stats
        val currentTime = ZombieHero.plugin.getTime()
        val passedTick = currentTime - lastShot
        if (passedTick < rate.tick) {
            return
        }
        lastShot = currentTime
        stats.currentArmo += -1
        shoot(player, gun)
    }

    fun shoot(player: Player, gun: Gun) {
        PlayerForceRecoil.sendRecoilPacket(player, -gun.recoilX,-gun.recoilY)
        val gunSound= gun.shotSound
        player.world.playSound(player.location, gunSound.type.sound, gunSound.volume, gunSound.pitch)
        val dir = player.location.direction
        player.location.direction = dir.multiply(1)
        val bullet = player.location.clone().apply {y+=player.eyeHeight }
        (1..shootAmmo).forEach { _ ->
            shootArmo(bullet.clone(), player)
        }
    }

    var lastShot: Int

    val rate: Tick
    val damage: Int
    val knockBack: Double
    val shootAmmo: Int
    val accuracy: Accuracy
    val particle: Particle
}
