package click.recraft.zombiehero

import click.recraft.zombiehero.ZombieHeroData.damagePluginHealth
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.scheduler.BukkitTask

class PlayerDamage: Listener {
    private fun spawnFloatingDamage(location: Location, damage: Int) {
        val range = arrayListOf<Double>().apply {add(0.5); add(-0.5); add(0.3); add(-0.3); add(0.7); add(0.7)}
        val loc = location.clone().apply {x += range.random(); y ; z += range.random()}
        val entity = (loc.world!!.spawnEntity(loc, EntityType.ARMOR_STAND) as ArmorStand).apply {
            isInvisible = true
            isMarker = true
            isCustomNameVisible = true
            customName = "${ChatColor.GOLD}$damage"
            isInvulnerable = true
            setGravity(false)
            isSilent = true
            isCollidable = false
            noDamageTicks = 10000000
            removeWhenFarAway = true
            setAI(false)
            isSmall = true
        }
        val task: (BukkitTask) -> Unit = {
            entity.remove()
        }
        Bukkit.getScheduler().runTaskLater(ZombieHero.plugin, task,20)
    }
    // プレイヤーが攻撃されたらHpを更新して､Actionbarを更新して､
    @EventHandler
    fun player(event: EntityDamageEvent) {
        val player = if (event.entity !is Player) return else event.entity as Player
        player.damagePluginHealth(event.damage.toInt())
        event.damage = 0.0
    }
}