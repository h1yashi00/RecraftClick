package click.recraft.zombiehero.item.skill

import click.recraft.zombiehero.player.PlayerData.addAmmo
import click.recraft.zombiehero.player.PlayerData.usingGun
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.player.PlayerInteractEvent

class AmmoBox: SkillItem(
    Material.COAL,
    arrayListOf(
        "${ChatColor.WHITE}右クリックすると､メインウェポンのマガジン*3個分の弾丸を入手することができる"
    ),
    "${ChatColor.WHITE}AmmoBox"
) {
    override fun inInvItemClick(clickType: ClickType, player: Player) {
    }

    override fun rightClick(event: PlayerInteractEvent) {
        event.isCancelled = true
        val player = event.player
        player.world.playSound(player.location, Sound.ENTITY_WITHER_SHOOT, 2f, 2f)
        val addAmmo = player.usingGun()!!.stats.maxArmo * 3
        player.addAmmo(addAmmo)
        player.inventory.removeItem(createItemStack())
    }

    override fun leftClick(event: PlayerInteractEvent) {
        event.isCancelled = true
    }
}