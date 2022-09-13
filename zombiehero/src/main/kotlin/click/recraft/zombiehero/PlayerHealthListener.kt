package click.recraft.zombiehero

import click.recraft.zombiehero.player.HealthManager.damagePluginHealth
import click.recraft.zombiehero.player.HealthManager.healPluginHealth
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityRegainHealthEvent

class PlayerHealthListener: Listener {
    // プレイヤーが攻撃されたらHpを更新して､Actionbarを更新して､

    @EventHandler
    fun health(event: EntityRegainHealthEvent) {
        val player = event.entity
        if (player !is Player) return
        player.healPluginHealth(event.amount.toInt())
    }
    @EventHandler
    fun player(event: EntityDamageEvent) {
        val player = if (event.entity !is Player) return else event.entity as Player
        player.damagePluginHealth(event.damage.toInt())
        event.damage = 0.0
    }
    @EventHandler
    fun playerDamage(event: EntityDamageEvent) {
        val player = if (event.entity !is Player) return else event.entity as Player
        player.damagePluginHealth(event.damage.toInt())
        event.damage = 0.0
    }
    @EventHandler
    fun entityDamageByPlayer(event: EntityDamageByEntityEvent) {
        if (event.damager is Player) {event.damager} else return
        event.isCancelled = true
    }
}