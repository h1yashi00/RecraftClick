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
    @EventHandler
    fun health(event: EntityRegainHealthEvent) {
        val player = event.entity
        if (player !is Player) return
        player.healPluginHealth(event.amount.toInt())
    }

    @EventHandler
    fun damage(event: EntityDamageEvent) {
        val player = event.entity
        if (player !is Player) return
        if (event.cause != EntityDamageEvent.DamageCause.FALL) return
        event.isCancelled = true
        player.damagePluginHealth(player, event.damage.toInt(), null)
    }
    @EventHandler
    fun entityDamageByPlayer(event: EntityDamageByEntityEvent) {
        if (event.damager is Player) {event.damager} else return
        event.isCancelled = true
    }
}