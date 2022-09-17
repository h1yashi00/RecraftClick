package click.recraft.zombiehero.command

import click.recraft.zombiehero.monster.api.MonsterManager
import click.recraft.zombiehero.monster.api.MonsterType
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class MonsterCommand: CommandExecutor {
    private val manager = MonsterManager
    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): Boolean {
        val player = if (sender is Player) sender else return false
        if (!player.isOp) return false
        val type = if (args[0] == "skeleton") {
            MonsterType.SKELETON
        } else if (args[0] == "zombie"){
            MonsterType.ZOMBIE
        }
        else {
            return false
        }
        manager.register(player, type)
        return true
    }
}