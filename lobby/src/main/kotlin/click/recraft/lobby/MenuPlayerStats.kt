package click.recraft.lobby

import click.recraft.share.*
import click.recraft.share.protocol.Database
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
        player.openInventory(menu.createInv(player))
    }
    private val menu = Main.plugin.menu(
        "ステータス",
        true
    ) {
        slot(0,0, item(Material.PAPER, displayName = "ステータス")) {
            onRender {
                Database.getPlayerDataSync(player) {
                    setLore(
                        listOf(
                            "${ChatColor.WHITE}現在の名前: $currentName",
                            "${ChatColor.WHITE}初参加: $firstJoin",
                            "${ChatColor.WHITE}最後に参加した : $lastJoin",
                            "${ChatColor.WHITE}最後に退出した:  $lastLogout",
                        )
                    )
                }
            }
        }
        slot(0,1, item(Material.LIME_DYE, displayName = "リソースパックを読み込む")) {
            onClick {
                Database.changeAutoLoadResourcePack(player)
                player.closeInventory()
            }
            onRender {
                Database.getPlayerOption(player) {
                    selectedColoredGreenDye(autoResourcePackDownload)
                }
            }
        }
    }
}