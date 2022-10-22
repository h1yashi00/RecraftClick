package click.recraft.lobby

import click.recraft.share.*
import click.recraft.share.database.PlayerManager
import org.bukkit.ChatColor
import org.bukkit.Material

object MenuPlayerStats {
    fun load() {
    }
    val item = Main.plugin.interactItem(
        item(Material.BOOK, displayName = "${ChatColor.WHITE}ステータス", customModelData = 10),
        rightClick = true,
        leftClick = false
    ) {
        openMenu(menu)
    }
    private val menu = Main.plugin.menu(
        "ステータス",
        true
    ) {
        slot(0,0, item(Material.IRON_SWORD, displayName = "ゲームステータス")) {
            onClick {
            }
            onRender {
                PlayerManager.getClonedData(player).apply {
                    setLore(
                        listOf(
                            "${ChatColor.WHITE}コイン: $coin",
                            "${ChatColor.WHITE}プレイした: $timesPlayed",
                            "${ChatColor.WHITE}モンスターを倒した: $monsterKills",
                            "${ChatColor.WHITE}銃で倒した: $gunKills",
                            "${ChatColor.WHITE}近接武器で倒した: $meleeKills",
                            "${ChatColor.WHITE}感染させた: $humanKills",
                        )
                    )
                }
            }
        }
        slot(0,7, item(Material.PAPER, displayName = "サーバステータス")) {
            onRender {
                PlayerManager.getClonedData(player).apply {
                    setLore(
                        listOf(
                            "${ChatColor.WHITE}現在の名前: $name",
                            "${ChatColor.WHITE}初参加: $firstLogin",
                            "${ChatColor.WHITE}最後に参加した : $lastLogin",
                            "${ChatColor.WHITE}最後に退出した:  $lastLogout",
                        )
                    )
                }
            }
        }
        slot(0,8, item(Material.LIME_DYE, displayName = "リソースパックを読み込む")) {
            onClick {
                PlayerManager.changeAutoLoadResourcePack(player)
                player.closeInventory()
            }
            onRender {
                selectedColoredGreenDye(
                    PlayerManager.getClonedData(player).autoLoadResourcePack
                )
            }
        }
    }
}