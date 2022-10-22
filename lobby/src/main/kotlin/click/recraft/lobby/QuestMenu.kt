package click.recraft.lobby

import click.recraft.share.*
import click.recraft.share.database.PlayerManager
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.inventory.ItemStack

object QuestMenu {
    fun load() {}
    private fun getQuestMenuItem(dailyQuest: PlayerManager.DailyQuest): ItemStack {
        val displayName = "${ChatColor.WHITE}${ChatColor.BOLD}デイリークエスト"
        val quest = dailyQuest.quest
        val loreEnd = "${ChatColor.GOLD}獲得コイン: ${quest.giveCoin}"
        return when {
            dailyQuest.isFinished -> item(Material.MAP,   displayName = displayName, lore = listOf(dailyQuest.quest.displayName, "${ChatColor.GREEN}完了"))
            dailyQuest.isReceived -> item(Material.PAPER, displayName = displayName, lore = listOf(dailyQuest.quest.displayName, "${ChatColor.WHITE}${dailyQuest.counter} / ${quest.finishNum}", loreEnd), enchanted = true)
            else ->                     item(Material.PAPER, displayName = displayName, lore = listOf(dailyQuest.quest.displayName, "${ChatColor.WHITE}${dailyQuest.counter} / ${quest.finishNum}", loreEnd))
        }
    }
    val menu = Main.plugin.menu("デイリークエストメニュー", true) {
        PlayerManager.QuestColumn.values().forEachIndexed { i, questColumn ->
            slot(i, item(Material.PAPER)) {
                onClick {
                    val dailyQuest = PlayerManager.getClonedData(player).dailyQuests[questColumn]!!
                    if (dailyQuest.isReceived) {
                        if (!dailyQuest.isFinished) {
                            PlayerManager.dailyQuestFinish(player, questColumn)
                        }
                    }
                    else {
                        PlayerManager.receivedQuest(player, questColumn)
                        player.playSound(player, Sound.ENTITY_ALLAY_AMBIENT_WITH_ITEM, 1F, 1F)
                    }
                    player.closeInventory()
                }
                onRender {
                    PlayerManager.getClonedData(player).apply {
                        setItem(getQuestMenuItem(dailyQuests[questColumn]!!))
                    }
                }
            }
        }
    }
}