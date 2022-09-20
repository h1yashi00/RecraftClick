package click.recraft.zombiehero.item.skill

import click.recraft.zombiehero.Util
import click.recraft.zombiehero.ZombieHero
import click.recraft.zombiehero.player.PlayerData.removePlayerSkillHeadShot
import click.recraft.zombiehero.player.PlayerData.setPlayerSkillHeadShot
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.player.PlayerInteractEvent

class HeadShot: SkillItem (
    Material.SKELETON_SKULL
) {
    override fun inInvItemClick(clickType: ClickType, player: Player) {
    }

    override fun rightClick(event: PlayerInteractEvent) {
        event.isCancelled = true
        val player = event.player
        player.inventory.remove(createItemStack())
        player.setPlayerSkillHeadShot()
        createPassenger(player)
        val task = Util.createTask {
            removePassenger(player)
            player.removePlayerSkillHeadShot()
        }
        Bukkit.getScheduler().runTaskLater(ZombieHero.plugin, task, 20 * 5)
    }

    override fun leftClick(event: PlayerInteractEvent) {
        event.isCancelled = true
    }
}