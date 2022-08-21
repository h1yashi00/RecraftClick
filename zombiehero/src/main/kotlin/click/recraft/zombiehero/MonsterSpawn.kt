package click.recraft.zombiehero

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntitySpawnEvent

class MonsterSpawn: Listener {
    private val monsterHealth = hashMapOf<Int, Int>()
    @EventHandler
    fun monsterSpawn(event: EntitySpawnEvent) {
        val livingEntity = if (event.entity is LivingEntity) {event.entity as LivingEntity} else return
        if (livingEntity.type == EntityType.ARMOR_STAND) return
        livingEntity.isCustomNameVisible = true
        val hp = 1000
        monsterHealth[livingEntity.entityId] = hp
        livingEntity.customName = "${ChatColor.WHITE}${livingEntity.name}${ChatColor.RED}♥$hp"
    }
    @EventHandler
    fun damageByEntity(event: EntityDamageEvent) {
        if (event.cause == EntityDamageEvent.DamageCause.ENTITY_ATTACK) return
        val livingEntity = if (event.entity is LivingEntity) {event.entity as LivingEntity} else return
        if (livingEntity is Player) return
        val damage = event.damage
        val hp = monsterHealth[livingEntity.entityId]
        if (hp == null) {
            livingEntity.health = 0.0
            return
        }
        val currentHp = hp - damage.toInt()
        monsterHealth[livingEntity.entityId] = currentHp
        if (currentHp < 0) {
            monsterHealth.remove(livingEntity.entityId)
            livingEntity.health = 0.0
            return
        }
        livingEntity.customName = ""
        livingEntity.customName = "${ChatColor.WHITE}${livingEntity.name}${ChatColor.RED}♥$currentHp"
        event.damage = 0.0
    }
}