package click.recraft.zombiehero.gun.api

import click.recraft.share.extension.runTaskLater
import click.recraft.zombiehero.UseNms
import click.recraft.zombiehero.Util
import click.recraft.zombiehero.ZombieHero
import click.recraft.zombiehero.event.BulletHitBlock
import click.recraft.zombiehero.event.BulletHitLivingEntityEvent
import click.recraft.zombiehero.item.gun.Gun
import click.recraft.zombiehero.player.HealthManager.damagePluginHealth
import org.bukkit.*
import org.bukkit.block.Block
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
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

    private fun rand(scope: Boolean): Double {
        val rangeMin = if (scope) {
            -accuracy.scopedValue
        }
        else {
            -accuracy.value
        }
        val rangeMax = if (scope) {
            accuracy.scopedValue
        }
        else {
            accuracy.value
        }
        return rangeMin + (rangeMax - rangeMin) * Random().nextDouble()
    }

    fun bulletRange(): Int {
        return 100
    }

    private fun shootArmo(bullet: Location, player: Player, gun: Gun) {
        bullet.apply { direction = Vector(direction.x + rand(gun.isScoping()), direction.y + rand(gun.isScoping()), direction.z + rand(gun.isScoping())) }
        (0..(bulletRange() * 2)).forEach{ _ ->
            showBallistic(bullet)
            bullet.apply { x += (direction.x / 2) ; y += (direction.y / 2); z += (direction.z /2)}
            if (bullet.block.type.isSolid) {
                bulletHitBlockEffect(bullet.block)
                Bukkit.getPluginManager().callEvent(BulletHitBlock(bullet.block, player))
                return
            }
            val bulletBoundingBox = Util.makeBoundingBox(bullet,0.1)
            val entities = bullet.world!!.getNearbyEntities(bulletBoundingBox)
            if (entities.isNotEmpty()) {
                if (entities.contains(player)) { return@forEach }
                val entity = entities.first()
                if (entity.isDead) { return@forEach }
                if (entity is ArmorStand) return@forEach
                // ???????????????????????????????????????????????????
                if (entity !is LivingEntity) return@forEach
                val event = BulletHitLivingEntityEvent (
                    player,
                    bulletLocation = bullet.clone(),
                    hitLivingEntity = entity,
                    knockBack = knockBack,
                    damage = damage.toDouble(),
                    bulletBoundingBox = bulletBoundingBox
                )
                Bukkit.getPluginManager().callEvent(event)
                if (event.isCancelled) {
                    return
                }
                event.hitLivingEntity.damagePluginHealth(event.shooter, event.damage.toInt(), gun, Util.isHeadLocation(bulletBoundingBox, entity.eyeLocation.clone()))
                val dir = event.shooter.location.direction.clone()
                event.hitLivingEntity.noDamageTicks = 0
                if (saveKnockBack.containsKey(event.hitLivingEntity.uniqueId)) {
                    val data = saveKnockBack[event.hitLivingEntity.uniqueId]!!
                    val newValue = data + knockBack
                    saveKnockBack[event.hitLivingEntity.uniqueId] = newValue
                }
                else {
                    saveKnockBack[event.hitLivingEntity.uniqueId] = knockBack
                    runTaskLater(1) {
                        val accumulateKnockBack = saveKnockBack[event.hitLivingEntity.uniqueId]!!
                        event.hitLivingEntity.velocity = if (event.hitLivingEntity is Player) {
                            if ((event.hitLivingEntity as Player).gameMode == GameMode.SPECTATOR) {
                                Vector()
                            }
                            else {
                                dir.multiply(accumulateKnockBack).apply { y = 0.1 }
                            }
                        }
                        else {
                            dir.multiply(accumulateKnockBack).apply { y = 0.1 }
                        }
                        saveKnockBack.remove(event.hitLivingEntity.uniqueId)
                    }
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
        UseNms.sendRecoilPacket(player, -gun.recoilX,-gun.recoilY)
        val gunSound= gun.shotSound
        player.world.playSound(player.location, gunSound.type.sound, gunSound.volume, gunSound.pitch)
        val bullet = player.location.clone().apply {y+=player.eyeHeight }
        (1..shootAmmo).forEach { _ ->
            shootArmo(bullet.clone(), player, gun)
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
