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
        // デバッグの場合はopである必要がない｡
        if (System.getenv("SERVER_NAME") != "debug") {
            if (!player.isOp) return false
        }
        if (args.size != 1) {
            player.sendMessage("need args [main,sub,skill,melee]")
            return false
        }
        val type = args[0]
        player.inventory.clear()
        MonsterManager.remove(player)
        val items = when (type) {
            "main" ->
                Item.getMain()
            "sub" ->
                Item.getSub()
            "skill" ->
                Item.getSkill()
            "melee" ->
                Item.getMelee()
            else -> {
                arrayListOf()
            }
        }
        items.forEach {
            player.inventory.addItem(
                ZombieHero.plugin.customItemFactory.create(it).createItemStack()
            )
        }
        return true
    }
}