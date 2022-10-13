package click.recraft.lobby

import click.recraft.share.*
import click.recraft.share.protocol.Database
import click.recraft.share.protocol.TextureItem
import org.bukkit.ChatColor
import org.bukkit.Material

object MenuPlayerZombieHeroStats {
    fun load(){}
    val item = Main.plugin.interactItem(item(Material.GOLD_INGOT, displayName = "${ChatColor.GOLD}Shop"),
        rightClick = true,
        leftClick = false
    ) {
        player.openInventory(menu.createInv(player))
    }
    private val menu = Main.plugin.menu("Shop", true) {
        TextureItem.values().forEachIndexed { i, textureItem ->
            slot(i, item = textureItem.getItem()) {
                onClick {
                }
                onRender {
                    Database.getPlayerZombieHeroItem(player) {
                        setUnlock(contains(textureItem.itemType))
                    }
                }
            }
        }
    }
}