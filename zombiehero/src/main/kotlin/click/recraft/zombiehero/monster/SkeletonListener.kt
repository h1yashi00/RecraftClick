package click.recraft.zombiehero.monster

import click.recraft.zombiehero.Util
import click.recraft.zombiehero.ZombieHero
import org.bukkit.Bukkit
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.entity.Projectile
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityShootBowEvent
import org.bukkit.event.entity.ProjectileHitEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerMoveEvent
import java.util.*
import kotlin.collections.HashMap

class SkeletonListener(
    private val monsterManager: MonsterManager
): Listener {
    @EventHandler
    fun rightClick(event: PlayerInteractEvent) {
        val action = event.action
        if (!(action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK)) {
            return
        }
        val monster = monsterManager.get(event.player) ?: return
        if (monster !is Skeleton) return
        monster.rightClick(event)
    }
    private val save: HashMap<UUID, Projectile> = hashMapOf()

    @EventHandler
    fun damage(event: EntityDamageByEntityEvent) {
        val player = if (event.entity is Player) {event.entity as Player} else return
        val monster = monsterManager.get(player)
        val skeleton = if (monster is Skeleton) {monster as Skeleton} else return
        val loc = player.location
        loc.world!!.playSound(loc, Sound.ENTITY_SKELETON_HURT,  1F,1F)
    }

    var soundDelay = ZombieHero.plugin.getTime()
    @EventHandler
    fun move(event: PlayerMoveEvent) {
        val player = event.player
        val monster = monsterManager.get(player) ?: return
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
        val monster = monsterManager.get(player) ?: return
        if (monster !is Skeleton) return
        val projectile = event.projectile as Projectile
        save[projectile.uniqueId] = projectile
    }

    @EventHandler
    fun entity(event: ProjectileHitEvent) {
        val projectile = save[event.entity.uniqueId] ?: return
        val player = projectile.shooter
        if (player !is Player) return
        val monster = monsterManager.get(player) ?: return
        if (monster !is Skeleton) return
        val entity = event.hitEntity ?: return
        monster.walkSpeed += 10
        val task = Util.createTask {
            monster.walkSpeed -= 10
        }
        Bukkit.getScheduler().runTaskLater(ZombieHero.plugin, task, 20 * 5)
        save.remove(event.entity.uniqueId)
    }
}