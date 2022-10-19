package click.recraft.lobby

import click.recraft.share.*
import click.recraft.share.database.PlayerManager
import click.recraft.share.protocol.TextureItem
import org.bukkit.ChatColor
import org.bukkit.Material

object MenuPlayerZombieHeroStats {
    fun load(){}
    val item = Main.plugin.interactItem(item(Material.GOLD_INGOT, displayName = "${ChatColor.GOLD}Shop"),
        rightClick = true,
        leftClick = false
    ) {
        openMenu(menu)
    }
    private val menu = Main.plugin.menu("Shop Menu", true) {
        slot(0, item= TextureItem.MAIN_AK47.getItem(displayName = "${ChatColor.WHITE}メインウェポンを買う")) {
            onClick {
                player.openInventory(menuMain.createInv(player))
            }
        }
        slot(1, item = TextureItem.SUB_DESERT_EAGLE.getItem(displayName = "${ChatColor.WHITE}サブウェポンを買う")) {
            onClick {
                player.openInventory(menuSub.createInv(player))
            }
        }
        slot(2, item = TextureItem.MELEE_NATA.getItem(displayName = "${ChatColor.WHITE}近接武器を買う")) {
            onClick {
                player.openInventory(meleeMenu.createInv(player))
            }
        }
        slot(3, item = TextureItem.SKILL_AMMO_DUMP.getItem(displayName = "${ChatColor.WHITE}Skillを買う")) {
            onClick {
                player.openInventory(skillMenu.createInv(player))
            }
        }
    }
    private val menuMain = Main.plugin.menu("Shop Main", true) {
        TextureItem.getMain().forEachIndexed { i, textureItem ->
            slot(i, item = textureItem.getItem()) {
                onClick {
                    openInv(itemMenus[textureItem]!!)
                }
                onRender {
                    val data = PlayerManager.get(player)
                    val item = textureItem.getItemWithPriceUnlock(data).clone()
                    setItem(item)
                }
            }
        }
    }
    private val menuSub = Main.plugin.menu("Shop Sub", true) {
        TextureItem.getSub().forEachIndexed { i, textureItem ->
            slot(i, item = textureItem.getItem()) {
                onClick {
                    openInv(itemMenus[textureItem]!!)
                }
                onRender {
                    val data = PlayerManager.get(player)
                    val item = textureItem.getItemWithPriceUnlock(data).clone()
                    setItem(item)
                }
            }
        }
    }
    private val meleeMenu = Main.plugin.menu("Shop Melees", true) {
        TextureItem.getMelee().forEachIndexed { i, textureItem ->
            slot(i, item = textureItem.getItem()) {
                onClick {
                    openInv(itemMenus[textureItem]!!)
                }
                onRender {
                    val entity = PlayerManager.get(player)
                    val item = textureItem.getItemWithPriceUnlock(entity).clone()
                    setItem(item)
                }
            }
        }
    }
    private val skillMenu = Main.plugin.menu("Shop Skill", true) {
        TextureItem.getSkill().forEachIndexed { i, textureItem ->
            slot(i, item = textureItem.getItem()) {
                onClick {
                    openInv(itemMenus[textureItem]!!)
                }
                onRender {
                    val entity = PlayerManager.get(player)
                    val item = textureItem.getItemWithPriceUnlock(entity).clone()
                    setItem(item)
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
                            val entity = PlayerManager.get(player)
                            val item = textureItem.getItemWithPriceUnlock(entity).clone()
                            setItem(item)
                        }
                    }

                    slot (1, item= item(Material.GOLD_INGOT)) {
                        onRender {
                            val data = PlayerManager.get(player)
                            setItem(item(Material.GOLD_INGOT, displayName = "${ChatColor.GOLD}所有コイン: ${data.coin}"))
                        }
                    }

                    slot(2, item = item(Material.GREEN_WOOL, displayName = "${ChatColor.GREEN}購入する")) {
                        onClick {
                            player.closeInventory()
                            PlayerManager.unlock(player, textureItem.itemType)
                        }
                    }

                    slot(5, item(Material.RED_WOOL, displayName = "${ChatColor.RED}キャンセル")) {
                        onClick {
                            player.closeInventory()
                        }
                    }
                }
            )
        }
    }
}