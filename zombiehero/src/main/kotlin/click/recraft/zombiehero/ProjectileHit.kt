package click.recraft.zombiehero

import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Monster
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.ProjectileHitEvent

//class ProjectileHit: Listener {
//    @EventHandler
//    fun projectileHit(event: ProjectileHitEvent) {
//        event.isCancelled = true
//        val projectile = event.entity
//        var damage = 0
//        var velocity = 0.0
//        ZombieHero.plugin.guns.forEach {
//            val damageKey = "${it.name}_bullet_damage"
//            if (projectile.hasMetadata(damageKey)) {
//                damage= projectile.getMetadata(damageKey).first().asInt()
//            }
//            val velocityKey = "${it.name}_bullet_velocity"
//            if (projectile.hasMetadata(velocityKey)) {
//                velocity = projectile.getMetadata(velocityKey).first().asDouble()
//            }
//        }
//        if (damage == 0 && velocity == 0.0) return
//        val victim = if (event.hitEntity?.isDead ?: return) {return} else event.hitEntity as LivingEntity
//        val source = projectile.shooter
//        if (source !is Player) return
//        victim.velocity = source.location.direction.multiply(velocity).apply {y=0.2}
//        victim.noDamageTicks = 0
//        if (victim is Monster ) { (victim as Monster).target = source}
//    }
//}