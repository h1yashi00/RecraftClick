package click.recraft.zombiehero

import click.recraft.share.*
import click.recraft.zombiehero.item.CustomItemFactory.*
import click.recraft.zombiehero.monster.api.MonsterType
import click.recraft.zombiehero.player.PlayerData.grenade
import click.recraft.zombiehero.player.PlayerData.mainGunType
import click.recraft.zombiehero.player.PlayerData.monsterType
import click.recraft.zombiehero.player.PlayerData.setGrenade
import click.recraft.zombiehero.player.PlayerData.setMainGunType
import click.recraft.zombiehero.player.PlayerData.setMonsterType
import click.recraft.zombiehero.player.PlayerData.setSubGunType
import click.recraft.zombiehero.player.PlayerData.setSword
import click.recraft.zombiehero.player.PlayerData.subGunType
import click.recraft.zombiehero.player.PlayerData.sword
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player

object GameMenu {
    // DSLの使い方
    // クラスから生えた拡張関数を使うと､invokeする際に､拡張関数ではやしたクラスが必要になる
    // 拡張関数はそのクラスの内容を使用することができる
    // 拡張関数は任意のタイミングで実行させるようにすることができる

    // loadが呼び出されないとshop.getItem()を呼んだときに初期化されてしまうので､ここを呼び出す
    fun load() {
    }
    private val mainGunSelect = ZombieHero.plugin.interactItem(
        item(Material.DIAMOND_SWORD,
            displayName = "${ChatColor.GOLD}MainGunSelect",
            customModelData = 1000
        ),
        rightClick = true,
        leftClick  = false
    ) {
        player.openInventory(mainGunMenu.createInv(player))
    }
    private val subGunSelect = ZombieHero.plugin.interactItem(
        item(Material.WOODEN_SWORD,
            displayName = "${ChatColor.GOLD}SubGunSelect",
            customModelData = 300
        ),
        rightClick = true,
        leftClick  = false
    ) {
        player.openInventory(subGunMenu.createInv(player))
    }
    private val zombieSelect = ZombieHero.plugin.interactItem(
        item(
            Material.ZOMBIE_HEAD,
            displayName = "${ChatColor.GOLD}ZombieSelect",
            customModelData = 200
        ),
        rightClick = true,
        leftClick  = false
    ) {
        player.openInventory(monsterMenu.createInv(player))
    }
    private val sword = ZombieHero.plugin.interactItem(
        item(
            Material.IRON_SWORD,
            displayName = "${ChatColor.GOLD}Melee",
            customModelData = 100
        ),
        rightClick = true,
        leftClick = false
    ) {
        player.openInventory(swordMenu.createInv(player))
    }
    private val grenade = ZombieHero.plugin.interactItem(
        item(
            Material.SLIME_BALL,
            displayName = "${ChatColor.GOLD}Grenade",
            customModelData = 110
        ),
        rightClick = true,
        leftClick = false
    ) {
        player.openInventory(grenadeMenu.createInv(player))
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
    private val swordMenu = ZombieHero.plugin.menu ("${ChatColor.WHITE}近接武器メニュー", true) {
        SwordType.values().forEachIndexed { i , swordType ->
            slot(0, i, factory.createSword(swordType).createItemStack()) {}
            slot(1, i, item(Material.EMERALD)) {
                onClick {
                    player.setSword(swordType)
                    player.closeInventory()
                    player.playSound(player, Sound.BLOCK_NOTE_BLOCK_HARP, 2f,2f)
                    player.sendMessage("剣: ${swordType.name}を選択しました")
                }
                onRender {
                    selectedColoredGreenDye(swordType == player.sword())
                }
            }
        }
    }
    private val grenadeMenu = ZombieHero.plugin.menu ("${ChatColor.WHITE}グレネードメニュー", true) {
        GrenadeType.values().forEachIndexed { i, grenadeType ->
            slot(0, i, factory.createGrenade(grenadeType).createItemStack()) {}
            slot(1, i, item(Material.EMERALD)) {
                onClick {
                    player.setGrenade(grenadeType)
                    player.closeInventory()
                    player.playSound(player, Sound.BLOCK_NOTE_BLOCK_HARP, 2f,2f)
                    player.sendMessage("グレネード: ${grenadeType.name}を選択しました")
                }
                onRender {
                    selectedColoredGreenDye(grenadeType == player.grenade())
                }
            }
        }
    }

    fun join(player: Player) {
        player.inventory.addItem(
            mainGunSelect.getItem(),
            subGunSelect.getItem(),
            zombieSelect.getItem(),
            sword.getItem(),
            grenade.getItem()
        )
    }
}