package click.recraft.zombiehero.chat

import click.recraft.zombiehero.event.MonsterAttackPlayerEvent
import click.recraft.zombiehero.event.PlayerDeadPluginHealthEvent
import click.recraft.zombiehero.monster.api.MonsterManager
import click.recraft.zombiehero.player.PlayerData.isHeadShot
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class PlayerChatListener: Listener {
    private val gun = "\uE003"
    private val headshotBullet = "\uE001"
    private val headshotHead = "\uE002"
    private val zombieAttack = "\uE000"
    private fun Player.coloredName(): String {
        MonsterManager.get(this) ?: return "${ChatColor.WHITE}${name}${ChatColor.GRAY}"
        return "${ChatColor.GREEN}${name}${ChatColor.GRAY}"
    }

    private fun headShotLog(player: Player): String {
        if (player.isHeadShot()) {
            return "$gun$headshotBullet$headshotHead"
        }
        return gun
    }
    @EventHandler
    fun playerKillZombie(event: PlayerDeadPluginHealthEvent) {
        val victim = event.victim
        val attacker = event.attacker
        if (attacker !is Player) return
        Bukkit.broadcastMessage("${victim.coloredName()} ${headShotLog(victim)} ${attacker.coloredName()}")
    }

    @EventHandler
    fun zombieKillPlayer(event: MonsterAttackPlayerEvent) {
        val victim = event.victim
        val attacker = event.attacker
        Bukkit.broadcastMessage("${victim.coloredName()} $zombieAttack ${ChatColor.WHITE}${attacker.name}")
    }
}