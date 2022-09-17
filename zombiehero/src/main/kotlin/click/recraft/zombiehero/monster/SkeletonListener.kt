package click.recraft.zombiehero.monster

import click.recraft.zombiehero.Util
import click.recraft.zombiehero.ZombieHero
import click.recraft.zombiehero.monster.api.MonsterListener
import click.recraft.zombiehero.player.HealthManager.healPluginHealth
import org.bukkit.Bukkit
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.entity.Projectile
import org.bukkit.event.EventHandler
import org.bukkit.event.block.Action
import org.bukkit.event.entity.EntityShootBowEvent
import org.bukkit.event.entity.ProjectileHitEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerMoveEvent
import java.util.*
import kotlin.collections.HashMap

class SkeletonListener(
): MonsterListener() {
    @EventHandler
    fun rightClick(event: PlayerInteractEvent) {
        val action = event.action
        if (!(action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK)) {
            return
        }
        val monster = get(event.player) ?: return
        if (monster !is Skeleton) return
        monster.rightClick(event)
    }
    private val save: HashMap<UUID, Projectile> = hashMapOf()

    var soundDelay = ZombieHero.plugin.getTime()
    @EventHandler
    fun move(event: PlayerMoveEvent) {
        val player = event.player
        val monster = get(player) ?: return
        if (monster !is Skeleton) return
        if (monster.skill2.active) {
            player.world.spawnParticle(Particle.SMOKE_NORMAL, player.location, 1, 0.0,0.0,0.0, 0.0)
            if (soundDelay <= ZombieHero.plugin.getTime()) {
                player.world.playSound(player.location, Sound.ENTITY_SKELETON_STEP, 1F,1F)
                soundDelay = ZombieHero.plugin.getTime() + 10
            }
        }
    }

    @EventHandler
    fun bow(event: EntityShootBowEvent) {
        val player = event.entity
        if (player !is Player) return
        val monster = get(player) ?: return
        if (monster !is Skeleton) return
        val projectile = event.projectile as Projectile
        save[projectile.uniqueId] = projectile
    }

    @EventHandler
    fun entity(event: ProjectileHitEvent) {
        val projectile = save[event.entity.uniqueId] ?: return
        val player = projectile.shooter
        if (player !is Player) return
        val monster = get(player) ?: return
        if (monster !is Skeleton) return
        event.isCancelled = true

        val loc = player.location

        val entity = if (event.hitEntity is LivingEntity) {event.hitEntity as LivingEntity} else  return
        val loc2 = entity.location
        val dir = loc.subtract(loc2).toVector().normalize()
        entity.velocity = dir.multiply(0.3)
        entity.damage(10.0)
        monster.walkSpeed += 20
        player.healPluginHealth(50)
        val task = Util.createTask {
            monster.walkSpeed -= 20
        }
        Bukkit.getScheduler().runTaskLater(ZombieHero.plugin, task, 20 * 5)
        save.remove(event.entity.uniqueId)
    }
}