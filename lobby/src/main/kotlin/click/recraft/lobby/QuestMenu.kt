package click.recraft.lobby

import click.recraft.share.*
import click.recraft.share.database.PlayerManager
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.Sound

object QuestMenu {
    fun load() {}
    val menu = Main.plugin.menu("デイリークエストメニュー", true) {
        slot(0, item(Material.PAPER)) {
            onClick {
                player.playSound(player, Sound.ENTITY_ALLAY_AMBIENT_WITH_ITEM, 1F, 1F)
                PlayerManager.get(player).apply {
                    receiveDailyQuest(dailyQuest1)
                }
                closeInv()
            }
            onRender {
                PlayerManager.get(player).apply {
                    setItem(
                        item(
                            Material.PAPER,
                            displayName = dailyQuest1.displayName,
                            enchanted = dailyQuestReceived1,
                            lore = listOf(
                                "${ChatColor.WHITE}$dailyQuest1Counter / ${dailyQuest1.finishNum}"
                            )
                    ))
                }
            }
        }
        slot(1, item(Material.PAPER)) {
            onClick {
                player.playSound(player, Sound.ENTITY_ALLAY_AMBIENT_WITH_ITEM, 1F, 1F)
                PlayerManager.get(player).apply {
                    receiveDailyQuest(dailyQuest2)
                }
                closeInv()
            }
            onRender {
                PlayerManager.get(player).apply {
                    setItem(
                        item(
                            Material.PAPER,
                            displayName = dailyQuest2.displayName,
                            enchanted = dailyQuestReceived2,
                            lore = listOf(
                                "${ChatColor.WHITE}$dailyQuest2Counter / ${dailyQuest2.finishNum}"
                            )
                        ))
                }
            }
        }
        slot(2, item(Material.PAPER)) {
            onClick {
                player.playSound(player, Sound.ENTITY_ALLAY_AMBIENT_WITH_ITEM, 1F, 1F)
                PlayerManager.get(player).apply {
                    receiveDailyQuest(dailyQuest3)
                }
                closeInv()
            }
            onRender {
                PlayerManager.get(player).apply {
                    setItem(
                        item(
                            Material.PAPER,
                            displayName = dailyQuest3.displayName,
                            enchanted = dailyQuestReceived3,
                            lore = listOf(
                                "${ChatColor.WHITE}$dailyQuest3Counter / ${dailyQuest3.finishNum}"
                            )
                        ))
                }
            }
        }
    }
}