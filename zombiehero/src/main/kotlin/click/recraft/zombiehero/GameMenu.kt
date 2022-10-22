package click.recraft.zombiehero

import click.recraft.share.*
import click.recraft.share.database.Item
import click.recraft.share.database.PlayerManager
import click.recraft.zombiehero.monster.api.MonsterType
import click.recraft.zombiehero.player.PlayerData.monsterType
import click.recraft.zombiehero.player.PlayerData.setMonsterType
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player

object GameMenu {
    // DSLの使い方
    // クラスから生えた拡張関数を使うと､invokeする際に､拡張関数ではやしたクラスが必要になる
    // 拡張関数はそのクラスの内容を使用することができる
    // 拡張関数は任意のタイミングで実行させるようにすることができる

    // loadが呼び出されないとgetItem()を呼んだときに初期化されてしまうので､ここを呼び出す
    fun load() {
    }
    private val main = ZombieHero.plugin.interactItem(
        item(Material.DIAMOND_SWORD,
            displayName = "${ChatColor.GOLD}MainGunSelect",
            customModelData = 1000
        ),
        rightClick = true,
        leftClick  = false
    ) {
        player.openInventory(mainMenu.createInv(player))
    }
    private val mainMenu = ZombieHero.plugin.menu("${ChatColor.GOLD}メイン武器", true) {
        slot(0,1,item(Material.BLACK_DYE)) {
            onClick {
                player.openInventory(mainGunMenu.createInv(player))
            }
            onRender {
                PlayerManager.getClonedData(player).apply {
                    setItem(factory.create(itemMain).createItemStack())
                }
            }
        }
        slot(0,2,item(Material.PINK_DYE)) {
            onClick {
                player.openInventory(subGunMenu.createInv(player))
            }
            onRender {
                PlayerManager.getClonedData(player).apply {
                    setItem(factory.create(itemSub).createItemStack())
                }
            }
        }
        slot(0,3, item(Material.GRAY_DYE)) {
            onClick {
                player.openInventory(meleeMenu.createInv(player))
            }
            onRender {
                PlayerManager.getClonedData(player).apply {
                    setItem(factory.create(itemMelee).createItemStack())
                }
            }
        }
        slot(0,4, item(Material.GRAY_DYE)) {
            onClick {
                player.openInventory(skillMenu.createInv(player))
            }
            onRender {
                PlayerManager.getClonedData(player).apply {
                    setItem(factory.create(itemSkill).createItemStack())
                }
            }
        }
        slot(0, 7, item(Material.ZOMBIE_HEAD)) {
            onClick {
                player.openInventory(monsterMenu.createInv(player))
            }
            onRender {
                setItem(player.monsterType().head)
            }
        }
    }

    private val factory = ZombieHero.plugin.customItemFactory
    private val mainGunMenu = ZombieHero.plugin.menu ("${ChatColor.GOLD}メイン武器", true) {
        Item.getMain().forEachIndexed { i, gunType ->
            slot(0, i, factory.create(gunType).createItemStack()) {}
            slot (1, i, item(Material.GREEN_DYE)) {
                onClick {
                    if (!PlayerManager.changeMain(player, gunType)) {
                        player.sendMessage("${ChatColor.RED}開放していません")
                        return@onClick
                    }
                    player.closeInventory()
                    player.playSound(player, Sound.BLOCK_NOTE_BLOCK_HARP, 2f,2f)
                    player.sendMessage("メインウェポン: ${gunType.name}を選択しました")
                }
                onRender {
                    PlayerManager.getClonedData(player).apply {
                        selectedColoredGreenDye(gunType == itemMain)
                    }
                }
            }
        }
    }
    private val subGunMenu = ZombieHero.plugin.menu ("${ChatColor.GOLD}サブ武器", true) {
        Item.getSub().forEachIndexed { i, sub ->
            slot(0, i, factory.create(sub).createItemStack()) {}
            slot (1, i, item(Material.GREEN_DYE)) {
                onClick {
                    if (!PlayerManager.changeSub(player, sub)) {
                        player.sendMessage("${ChatColor.RED}開放していません")
                        return@onClick
                    }
                    player.closeInventory()
                    player.playSound(player, Sound.BLOCK_NOTE_BLOCK_HARP, 2f,2f)
                    player.sendMessage("サブウェポン: ${sub.name}を選択しました")
                }
                onRender {
                    PlayerManager.getClonedData(player).apply {
                        selectedColoredGreenDye(sub == itemSub)
                    }
                }
            }
        }
    }
    private val monsterMenu = ZombieHero.plugin.menu("${ChatColor.GREEN}モンスターメニュー", true) {
        MonsterType.values().forEachIndexed { i, monsterType ->
            slot(0, i, monsterType.head) {}
            slot(1, i, monsterType.head) {
                onClick {
                    player.setMonsterType(monsterType)
                    player.closeInventory()
                    player.playSound(player, Sound.BLOCK_NOTE_BLOCK_HARP, 2f,2f)
                    player.sendMessage("モンスタータイプ: ${monsterType.name}を選択しました")
                }
                onRender {
                    selectedColoredGreenDye(monsterType == player.monsterType())
                }
            }
        }
    }
    private val meleeMenu = ZombieHero.plugin.menu ("${ChatColor.WHITE}近接武器メニュー", true) {
        Item.getMelee().forEachIndexed { i, meleeType ->
            slot(0, i, factory.create(meleeType).createItemStack()) {}
            slot(1, i, item(Material.EMERALD)) {
                onClick {
                    if (!PlayerManager.changeMelee(player, meleeType) ) {
                        player.sendMessage("${ChatColor.RED}開放していません")
                        return@onClick
                    }
                    player.closeInventory()
                    player.playSound(player, Sound.BLOCK_NOTE_BLOCK_HARP, 2f,2f)
                    player.sendMessage("剣: ${meleeType.name}を選択しました")
                }
                onRender {
                    PlayerManager.getClonedData(player).apply {
                        selectedColoredGreenDye(meleeType == itemMelee)
                    }
                }
            }
        }
    }

    private val skillMenu = ZombieHero.plugin.menu("${ChatColor.WHITE}スキルメニュー", true) {
        Item.getSkill().forEachIndexed { i, skillType ->
            slot(0, i, factory.create(skillType).createItemStack()) {}
            slot(1, i, item(Material.EMERALD)) {
                onClick {
                    if (!PlayerManager.changeSkill(player, skillType)) {
                        player.sendMessage("${ChatColor.RED}開放していません")
                        return@onClick
                    }
                    player.closeInventory()
                    player.playSound(player, Sound.BLOCK_NOTE_BLOCK_HARP, 2f,2f)
                    player.sendMessage("スキル: ${skillType.name}を選択しました")
                }
                onRender {
                    PlayerManager.getClonedData(player).apply {
                        selectedColoredGreenDye(skillType == itemSkill)
                    }
                }
            }
        }
    }

    fun join(player: Player) {
        player.inventory.addItem(
            main.getItem()
        )
    }
}