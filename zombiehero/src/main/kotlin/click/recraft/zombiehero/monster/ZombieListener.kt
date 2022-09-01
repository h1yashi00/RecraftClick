package click.recraft.zombiehero.monster

import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.player.PlayerInteractEvent
import java.util.Random

class ZombieListener(private val monsterManager: MonsterManager): Listener {
    @EventHandler
    fun rightClick(event: PlayerInteractEvent) {
        val action = event.action
        if (!(action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK)) {
            return
        }
        val monster = monsterManager.get(event.player) ?: return
        if (monster !is Zombie) return
        monster.rightClick(event)
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun damage(event: EntityDamageEvent) {
        val player = event.entity
        if (player !is Player) return
        val monster = monsterManager.get(player) ?: return
        if (monster !is Zombie) return
        if (monster.skill2.active) {
            val damage = event.damage / 2
            event.damage = damage
            val random = Random().nextFloat() + 1.0F
            val pitch = if (random > 2.0F) {2.0F} else {random}
            player.world.playSound(player.location, Sound.BLOCK_ANVIL_PLACE, 0.5f, pitch)
        }
    }
}