package click.recraft.zombiehero.command

import click.recraft.share.database.Item
import click.recraft.zombiehero.ZombieHero
import click.recraft.zombiehero.monster.api.MonsterManager
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class GunCommand: CommandExecutor {
    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): Boolean {
        val player = if (sender is Player) sender else return false
        if (!player.isOp) return false
        player.inventory.clear()
        MonsterManager.remove(player)
        Item.getMain().forEach {
            player.inventory.addItem(
                ZombieHero.plugin.customItemFactory.create(it).createItemStack()
            )
        }
        return true
    }
}