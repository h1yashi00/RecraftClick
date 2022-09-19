package click.recraft.zombiehero.chat

import click.recraft.zombiehero.event.MonsterAttackPlayerEvent
import click.recraft.zombiehero.event.PlayerDeadPluginHealthEvent
import click.recraft.zombiehero.monster.api.MonsterManager
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class PlayerChatListener: Listener {

    private fun Player.coloredName(): String {
        MonsterManager.get(this) ?: return "${ChatColor.WHITE}${name}${ChatColor.GRAY}"
        return "${ChatColor.GREEN}${name}${ChatColor.GRAY}"
    }

    @EventHandler
    fun playerKillZombie(event: PlayerDeadPluginHealthEvent) {
        val victim = event.victim
        val attacker = event.attacker
        if (attacker !is Player) return
        Bukkit.broadcastMessage("${victim.coloredName()} was killed ${attacker.coloredName()}")
    }

    @EventHandler
    fun zombieKillPlayer(event: MonsterAttackPlayerEvent) {
        val victim = event.victim
        val attacker = event.attacker
        Bukkit.broadcastMessage("${victim.coloredName()} was killed ${attacker.coloredName()}")
    }
}