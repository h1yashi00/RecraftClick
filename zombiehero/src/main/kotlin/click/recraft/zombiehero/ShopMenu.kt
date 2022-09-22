package click.recraft.zombiehero

import click.recraft.share.*
import click.recraft.zombiehero.item.CustomItemFactory.*
import click.recraft.zombiehero.monster.api.MonsterType
import click.recraft.zombiehero.player.PlayerData.mainGunType
import click.recraft.zombiehero.player.PlayerData.monsterType
import click.recraft.zombiehero.player.PlayerData.setMainGunType
import click.recraft.zombiehero.player.PlayerData.setMonsterType
import click.recraft.zombiehero.player.PlayerData.setSubGunType
import click.recraft.zombiehero.player.PlayerData.subGunType
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.Sound

object GameMenu {
    // DSLの使い方
    // クラスから生えた拡張関数を使うと､invokeする際に､拡張関数ではやしたクラスが必要になる
    // 拡張関数はそのクラスの内容を使用することができる
    // 拡張関数は任意のタイミングで実行させるようにすることができる

    // loadが呼び出されないとshop.getItem()を呼んだときに初期化されてしまうので､ここを呼び出す
    fun load() {
    }
    val mainGunSelect = ZombieHero.plugin.interactItem(
        item(Material.DIAMOND_SWORD,
            displayName = "${ChatColor.GOLD}MainGunSelect",
            customModelData = 1000
        ),
        rightClick = true,
        leftClick  = false
    ) {
        player.openInventory(mainGunMenu.createInv(player))
    }
    val subGunSelect = ZombieHero.plugin.interactItem(
        item(Material.WOODEN_SWORD,
            displayName = "${ChatColor.GOLD}SubGunSelect",
            customModelData = 300
        ),
        rightClick = true,
        leftClick  = false
    ) {
        player.openInventory(subGunMenu.createInv(player))
    }
    val zombieSelect = ZombieHero.plugin.interactItem(
        item(Material.ZOMBIE_HEAD,
            displayName = "${ChatColor.GOLD}ZombieSelect",
            customModelData = 200
        ),
        rightClick = true,
        leftClick  = false
    ) {
        player.openInventory(monsterMenu.createInv(player))
    }
    private val factory = ZombieHero.plugin.customItemFactory
    private val mainGunMenu = ZombieHero.plugin.menu ("${ChatColor.GOLD}メイン武器", true) {
        MainGunType.values().forEachIndexed { i, gunType ->
            slot(0, i, factory.createMainGun(gunType).createItemStack()) {}
            slot (1, i, item(Material.GREEN_DYE)) {
                onClick {
                    player.setMainGunType(gunType)
                    player.closeInventory()
                    player.playSound(player, Sound.BLOCK_NOTE_BLOCK_HARP, 2f,2f)
                    player.sendMessage("メインウェポン: ${gunType.name}を選択しました")
                }
                onRender {
                    selectedColoredGreenDye(gunType == player.mainGunType())
                }
            }
        }
    }
    private val subGunMenu = ZombieHero.plugin.menu ("${ChatColor.GOLD}サブ武器", true) {
        SubGunType.values().forEachIndexed { i, gunType ->
            slot(0, i, factory.createSubGun(gunType).createItemStack()) {}
            slot (1, i, item(Material.GREEN_DYE)) {
                onClick {
                    player.setSubGunType(gunType)
                    player.closeInventory()
                    player.playSound(player, Sound.BLOCK_NOTE_BLOCK_HARP, 2f,2f)
                    player.sendMessage("サブウェポン: ${gunType.name}を選択しました")
                }
                onRender {
                    selectedColoredGreenDye(gunType == player.subGunType())
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

}