package click.recraft.lobby

import click.recraft.share.*
import click.recraft.share.protocol.Database
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
        slot(0,0, item(Material.GOLD_INGOT, displayName = "player stats")) {
            onClick {
            }
            onRender {
                Database.getPlayerZombieHeroStats(player) {
                    setLore(listOf (
                        "${ChatColor.WHITE}コイン: $coin",
                        "${ChatColor.WHITE}プレイした: $timesPlayed",
                        "${ChatColor.WHITE}モンスターを倒した: $monsterKills",
                        "${ChatColor.WHITE}銃で倒した: $gunKills",
                        "${ChatColor.WHITE}近接武器で倒した: $meleeKills",
                        "${ChatColor.WHITE}感染させた: $humanKills",
                    ))
                }
            }
        }
    }
}