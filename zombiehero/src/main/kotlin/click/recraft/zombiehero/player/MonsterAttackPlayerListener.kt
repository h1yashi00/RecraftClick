package click.recraft.zombiehero.player

import click.recraft.zombiehero.ZombieHero
import click.recraft.zombiehero.event.MonsterAttackPlayerEvent
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class MonsterAttackPlayerListener(
): Listener {
    @EventHandler
    fun a(event: MonsterAttackPlayerEvent) {
        val victim = event.victim
        val world = victim.world
        world.playSound(victim.location, "minecraft:stab_knife_body", 1f,0.7f)
        if (ZombieHero.plugin.gameManager.checkGameCondition()) return
        world.playSound(victim.location, "minecraft:man_shout", 0.5f,1f)
    }
}