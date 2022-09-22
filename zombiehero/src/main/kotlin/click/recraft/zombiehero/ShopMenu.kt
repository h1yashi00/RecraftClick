package click.recraft.zombiehero

import click.recraft.share.*
import click.recraft.zombiehero.item.CustomItemFactory.*
import click.recraft.zombiehero.monster.api.MonsterType
import click.recraft.zombiehero.player.PlayerData.gunType
import click.recraft.zombiehero.player.PlayerData.monsterType
import click.recraft.zombiehero.player.PlayerData.setGunType
import click.recraft.zombiehero.player.PlayerData.setMonsterType
import org.bukkit.ChatColor
import org.bukkit.Material

object GameMenu {
    // DSLの使い方
    // クラスから生えた拡張関数を使うと､invokeする際に､拡張関数ではやしたクラスが必要になる
    // 拡張関数はそのクラスの内容を使用することができる
    // 拡張関数は任意のタイミングで実行させるようにすることができる

    // loadが呼び出されないとshop.getItem()を呼んだときに初期化されてしまうので､ここを呼び出す
    fun load() {
    }
    val gunSelect = ZombieHero.plugin.interactItem(
        item(Material.EMERALD,
            displayName = "${ChatColor.GOLD}GunSelect",
            customModelData = 1000
        ),
        rightClick = true,
        leftClick  = false
    ) {
        player.openInventory(gunMenu.createInv(player))
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
    private val gunMenu = ZombieHero.plugin.menu ("${ChatColor.GOLD}武器ショップ", true) {
        GunType.values().forEachIndexed { i, gunType ->
            slot(0, i, factory.createGun(gunType).createItemStack()) {
            }
            slot (1, i, item(Material.GREEN_DYE)) {
                onClick {
                    player.setGunType(gunType)
                    player.closeInventory()
                }
                onRender {
                    if (gunType == player.gunType()) {
                        chose()
                    }
                }
            }
        }
    }
    private val monsterMenu = ZombieHero.plugin.menu("${ChatColor.GREEN}モンスターメニュー", true) {
        MonsterType.values().forEachIndexed { i, monsterType ->
            slot(0, i, monsterType.head) {
            }
            slot(1, i, monsterType.head) {
                onClick {
                    player.setMonsterType(monsterType)
                    player.closeInventory()
                }
                onRender {
                    if (monsterType == player.monsterType()) {
                        chose()
                    }
                }
            }
        }
    }

}