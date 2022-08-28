package click.recraft.zombiehero

import click.recraft.zombiehero.player.HealthManager.damagePluginHealth
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent

class PlayerDamage: Listener {
    // プレイヤーが攻撃されたらHpを更新して､Actionbarを更新して､
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
}