package click.recraft.share.protocol

import click.recraft.share.database.Item
import click.recraft.share.database.PlayerManager
import click.recraft.share.item
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

enum class TextureItem(val material: Material, val customModelData: Int, val itemType: Item) {
    MAIN_AK47(Material.BLACK_DYE, customModelData = 1,   Item.MAIN_AK47),
    MAIN_AWP(Material.BLACK_DYE, customModelData = 3,    Item.MAIN_AWP),
    MAIN_SAIGA(Material.BLACK_DYE, customModelData = 5,  Item.MAIN_SAIGA),
    MAIN_M870(Material.BLACK_DYE, customModelData = 7,   Item.MAIN_M870),
    MAIN_MP5(Material.BLACK_DYE, customModelData = 9,    Item.MAIN_MP5),
    MAIN_MOSIN(Material.BLACK_DYE, customModelData = 11, Item.MAIN_MOSIN),
    SUB_DESERT_EAGLE(Material.BLACK_DYE, customModelData = 13, Item.SUB_DESERT_EAGLE),
    SUB_GLOCK(Material.BLACK_DYE, customModelData = 15,     Item.SUB_GLOCK),
    MELEE_NATA(Material.PINK_DYE, customModelData = 1,      Item.MELEE_NATA),
    MELEE_HAMMER(Material.PINK_DYE, customModelData = 2,    Item.MELEE_HAMMER),
    SKILL_AMMO_DUMP(Material.COAL, customModelData= 1,      Item.SKILL_AMMO_DUMP),
    SKILL_GRENADE(Material.PINK_DYE, customModelData = 1,   Item.SKILL_GRENADE),
    SKILL_ZOMBIE_GRENADE(Material.PINK_DYE, customModelData = 1, Item.SKILL_ZOMBIE_GRENADE),
    SKILL_ZOMBIE_GRENADE_TOUCH(Material.PINK_DYE, customModelData = 2, Item.SKILL_ZOMBIE_GRENADE_TOUCH);

    companion object {
        fun getMain(): List<TextureItem> {
            return values().filter {
                it.name.startsWith("MAIN_", ignoreCase = true)
            }
        }
        fun getSub(): List<TextureItem> {
            return values().filter {
                it.name.startsWith("SUB_", ignoreCase = true)
            }
        }
        fun getMelee(): List<TextureItem> {
            return values().filter {
                it.name.startsWith("MELEE_", ignoreCase = true)
            }
        }
        fun getSkill(): List<TextureItem> {
            return values().filter {
                it.name.startsWith("SKILL_", ignoreCase = true)
            }
        }
    }
    fun getItem(): ItemStack {
        return item(material, customModelData = customModelData)
    }
    fun getItem(displayName: String, lore : ArrayList<String> = arrayListOf()): ItemStack {
        return item(material, customModelData = customModelData, displayName = displayName, lore = lore)
    }
    fun getItemWithPriceUnlock(data: PlayerManager.PlayerData): ItemStack {
        val boolean = data.isItemUnlocked(itemType)
        val msg = if (boolean) {
            "${ChatColor.GREEN}${itemType}開放済み"
        }
        else {
            "${ChatColor.RED}${itemType}まだ開放していません"
        }
        return getItem(displayName = msg, lore = arrayListOf("${ChatColor.GOLD}値段: ${itemType.price}"))
    }
}