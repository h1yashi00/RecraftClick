package click.recraft.zombiehero

import click.recraft.share.*
import org.bukkit.ChatColor
import org.bukkit.Material

object ShopMenu {
    // DSLの使い方
    // クラスから生えた拡張関数を使うと､invokeする際に､拡張関数ではやしたクラスが必要になる
    // 拡張関数はそのクラスの内容を使用することができる
    // 拡張関数は任意のタイミングで実行させるようにすることができる

    // loadが呼び出されないとshop.getItem()を呼んだときに初期化されてしまうので､ここを呼び出す
    fun load() {
    }
    val shop = ZombieHero.plugin.interactItem(
        item(Material.EMERALD,
            displayName = "${ChatColor.GOLD}ショップ",
            customModelData = 1000
        ),
        rightClick = true,
        leftClick  = false
    ) {
        player.openInventory(shopMenu.createInv(player))
    }
    private val weaponMenu = ZombieHero.plugin.menu ("${ChatColor.GOLD}武器ショップ", true) {
        slot(0, 1, item(Material.WOODEN_SWORD, displayName = "ak-47")) {
            onClick {
            }
//            onRender {
//                setUnlock( ZombieHeroPlayerData.getWeaponStats(player.uniqueId).ak47 )
//            }
        }
        slot(0, 2, item(Material.IRON_SWORD, displayName = "shotgun")) {
            onClick {
            }
//            onRender {
//                setUnlock( ZombieHeroPlayerData.getWeaponStats(player.uniqueId).shotgun )
//            }
        }
        slot(0, 3, item(Material.GOLDEN_SWORD, displayName = "handgun")) {
            onClick {
            }
//            onRender {
//                setUnlock( ZombieHeroPlayerData.getWeaponStats(player.uniqueId).hundgun)
//            }
        }
        slot(0, 4, item(Material.DIAMOND_SWORD, displayName = "sniper rifle")) {
            onClick {
            }
//            onRender {
//                setUnlock( ZombieHeroPlayerData.getWeaponStats(player.uniqueId).sniper)
//            }
        }
        slot(0, 5, item(Material.BOW, displayName = "machine gun")) {
            onClick {
            }
//            onRender {
//                setUnlock( ZombieHeroPlayerData.getWeaponStats(player.uniqueId).machineGun)
//            }
        }
    }
    private val monsterMenu = ZombieHero.plugin.menu("${ChatColor.GREEN}モンスターメニュー", true) {
        slot(0, 0, item(Material.ZOMBIE_HEAD, displayName = "zombie")) {
            onClick {
            }
//            onRender {
//                setUnlock( ZombieHeroPlayerData.getMonsterStats(player.uniqueId).zombie )
//            }
        }
        slot(0, 1, item(Material.SKELETON_SKULL, displayName = "skeleton")) {
            onClick {
            }
//            onRender {
//                setUnlock( ZombieHeroPlayerData.getMonsterStats(player.uniqueId).skeleton)
//            }
        }
        slot(0, 2, item(Material.WITCH_SPAWN_EGG, displayName = "witch")) {
            onClick {
            }
//            onRender {
//                setUnlock( ZombieHeroPlayerData.getMonsterStats(player.uniqueId).witch)
//            }
        }
    }

    private val shopMenu = ZombieHero.plugin.menu("${ChatColor.GOLD}ショップ", true) {
        slot(0, 1, item(Material.WOODEN_SWORD, displayName = "${ChatColor.GOLD}武器ショップ")) {
            onClick {
                player.closeInventory()
                player.openInventory(weaponMenu.createInv(player))
            }
        }
        slot(0, 3, item(Material.ZOMBIE_HEAD, displayName = "${ChatColor.GOLD}モンスターショップ")) {
            onClick {
                player.closeInventory()
                player.openInventory(monsterMenu.createInv(player))
            }
        }
    }
}