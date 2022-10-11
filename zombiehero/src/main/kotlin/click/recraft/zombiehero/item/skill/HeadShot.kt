package click.recraft.zombiehero.item.skill

import click.recraft.zombiehero.Util
import click.recraft.zombiehero.ZombieHero
import click.recraft.zombiehero.player.PlayerData.removePlayerSkillHeadShot
import click.recraft.zombiehero.player.PlayerData.setPlayerSkillHeadShot
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.player.PlayerInteractEvent

class HeadShot: SkillItem (
    Material.SKELETON_SKULL,
    arrayListOf(
        "${ChatColor.WHITE}HeadShotスキル発動中はすべての攻撃がヘッドショット判定になる"
    ),
    "${ChatColor.WHITE}HeadShot"
) {
    override fun inInvItemClick(clickType: ClickType, player: Player) {
    }

    override fun rightClick(event: PlayerInteractEvent) {
        event.isCancelled = true
        val player = event.player
        player.world.playSound(player.location, Sound.ENTITY_WITHER_SHOOT, 2f, 2f)
        player.inventory.remove(createItemStack())
        player.setPlayerSkillHeadShot()
        val task = Util.createTask {
            player.removePlayerSkillHeadShot()
        }
        Bukkit.getScheduler().runTaskLater(ZombieHero.plugin, task, 20 * 5)
    }

    override fun leftClick(event: PlayerInteractEvent) {
        event.isCancelled = true
    }
}