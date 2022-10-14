package click.recraft.share.protocol

import click.recraft.share.item
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

enum class TextureItem(val material: Material, val customModelData: Int, val itemType: ItemType) {
    GUN_AK47(Material.BLACK_DYE, customModelData = 1, ItemType.AK47),
    GUN_AWP(Material.BLACK_DYE, customModelData = 3, ItemType.AWP),
    GUN_SAIGA(Material.BLACK_DYE, customModelData = 5, ItemType.SAIGA),
    GUN_M870(Material.BLACK_DYE, customModelData = 7, ItemType.M870),
    GUN_MP5(Material.BLACK_DYE, customModelData = 9, ItemType.MP5),
    GUN_MOSIN(Material.BLACK_DYE, customModelData = 11, ItemType.MOSIN),
    GUN_DESERT_EAGLE(Material.BLACK_DYE, customModelData = 13, ItemType.DESERT_EAGLE),
    GUN_GLOCK(Material.BLACK_DYE, customModelData = 15, ItemType.GLOCK),
    MELEE_NATA(Material.PINK_DYE, customModelData = 1, ItemType.NATA),
    MELEE_HAMMER(Material.PINK_DYE, customModelData = 2, ItemType.HAMMER),
    SKILL_AMMO_DUMP(Material.COAL, customModelData= 1, ItemType.AMMO_DUMP),
    SKILL_GRENADE(Material.PINK_DYE, customModelData = 1, ItemType.GRENADE),
    SKILL_ZOMBIE_GRENADE(Material.PINK_DYE, customModelData = 1, ItemType.ZOMBIE_GRENADE),
    SKILL_ZOMBIE_GRENADE_TOUCH(Material.PINK_DYE, customModelData = 2, ItemType.ZOMBIE_HIT_GRENADE);

    companion object {
        fun getGuns(): List<TextureItem> {
            return values().filter {
                it.name.startsWith("GUN_", ignoreCase = true)
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
    fun getItemWithPriceUnlock(itemTypes: MutableSet<ItemType>): ItemStack {
        val msg = if (itemTypes.contains(itemType)) {
            "${ChatColor.GREEN}${itemType}開放済み"
        }
        else {
            "${ChatColor.RED}${itemType}まだ開放していません"
        }
        return getItem(displayName = msg, lore = arrayListOf("${ChatColor.GOLD}値段: ${itemType.price}"))
    }
}