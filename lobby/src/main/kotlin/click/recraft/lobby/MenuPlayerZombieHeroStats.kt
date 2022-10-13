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
                    val menu = itemMenus[textureItem]!!.createInv(player)
                    player.openInventory(menu)
                }
                onRender {
                    Database.getPlayerZombieHeroItem(player) {
                        val item = textureItem.getItemWithPriceUnlock(this).clone()
                        setItem(item)
                    }
                }
            }
        }
    }
    private val itemMenus = hashMapOf<TextureItem, MenuDSL>().apply {
        TextureItem.values().forEachIndexed { i, textureItem ->
            put(
                textureItem,
                Main.plugin.menu("Shop ${textureItem.itemType}", true) {
                    slot (0, item = textureItem.getItem()) {
                        onRender {
                            Database.getPlayerZombieHeroItem(player) {
                                setItem(textureItem.getItemWithPriceUnlock(this).clone())
                            }
                        }
                    }
                    slot (1, item= item(Material.GOLD_INGOT)) {
                        onRender {
                            Database.getPlayerZombieHeroStats(player) {
                                setItem(item(Material.GOLD_INGOT, displayName = "${ChatColor.GOLD}所有コイン: $coin"))
                            }
                        }
                    }

                    slot(2, item = item(Material.GREEN_WOOL, displayName = "${ChatColor.GREEN}購入する")) {
                        onClick {
                            player.closeInventory()
                            val msg = Database.unlockItem(player, textureItem.itemType)
                            player.sendMessage(msg)
                        }
                    }

                    slot(5, item(Material.RED_WOOL, displayName = "${ChatColor.RED}キャンセル")) {
                        onClick { player.closeInventory() }
                    }
                }
            )
        }
    }
}