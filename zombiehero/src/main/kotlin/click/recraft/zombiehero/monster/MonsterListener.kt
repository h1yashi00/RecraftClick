package click.recraft.zombiehero.monster

import click.recraft.zombiehero.ZombieHero
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent

abstract class MonsterListener(
    private val monsterManager: MonsterManager = ZombieHero.plugin.monsterManager
): Listener {
    fun get(entity: Entity): Monster? {
        val player = if (entity is Player) {entity} else return null
        return monsterManager.get(player)
    }
    @EventHandler
    fun damage(event: EntityDamageEvent) {
        val monster = get(event.entity) ?: return
        val loc = event.entity.location
        loc.world!!.playSound(loc,monster.damageSound, 1f,1f)
    }
}