package click.recraft.lobby

import click.recraft.share.*
import click.recraft.share.database.PlayerManager
import click.recraft.share.extension.runTaskAsync
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractAtEntityEvent

class QuestMasterAllay: Listener {
    private val world = Bukkit.getWorld("world")!!
    private val allay = world.spawnEntity(world.spawnLocation.apply{chunk.load()}, EntityType.ALLAY).apply {
        if (this is LivingEntity) {
            setAI(false)
        }
        ticksLived  = 999999999
        isSilent = true
        isCustomNameVisible = true
        customName = "${ChatColor.GREEN}クエスト"
    }
    private val menu = Main.plugin.menu("menu", true) {
        slot(0, item(Material.PAPER)) {
            onRender {
                onClick {
                    PlayerManager.get(player).apply {
                        receiveDailyQuest(dailyQuest1)
                        runTaskAsync {
                            update()
                        }
                    }
                }
                PlayerManager.get(player).apply {
                    setItem(
                        item(
                            Material.PAPER,
                            displayName = "$dailyQuest1",
                            enchanted = dailyQuestReceived1,
                            lore = listOf(
                                "${dailyQuest1.finishNum}/ $dailyQuest1Counter"
                            )
                    ))
                }
            }
        }
        slot(1, item(Material.PAPER)) {
            onRender {
                onClick {
                    PlayerManager.get(player).apply {
                        receiveDailyQuest(dailyQuest2)
                        runTaskAsync {
                            update()
                        }
                    }
                }
                PlayerManager.get(player).apply {
                    setItem(
                        item(
                            Material.PAPER,
                            displayName = "$dailyQuest1",
                            enchanted = dailyQuestReceived2,
                            lore = listOf(
                                "${dailyQuest2.finishNum}/ $dailyQuest2Counter"
                            )
                        ))
                }
            }
        }
        slot(2, item(Material.PAPER)) {
            onRender {
                onClick {
                    PlayerManager.get(player).apply {
                        receiveDailyQuest(dailyQuest3)
                        runTaskAsync {
                            update()
                        }
                    }
                }
                PlayerManager.get(player).apply {
                    setItem(
                        item(
                            Material.PAPER,
                            displayName = "$dailyQuest1",
                            enchanted = dailyQuestReceived3,
                            lore = listOf(
                                "${dailyQuest3.finishNum}/ $dailyQuest3Counter"
                            )
                        ))
                }
            }
        }
    }
    @EventHandler
    fun playerInteractAllay(event: PlayerInteractAtEntityEvent) {
        if (event.rightClicked != allay) return
        val player = event.player
        player.openInventory(menu.createInv(player))
    }
}