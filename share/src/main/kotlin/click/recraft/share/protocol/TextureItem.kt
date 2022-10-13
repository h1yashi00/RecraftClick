package click.recraft.share.protocol

import click.recraft.share.item
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

enum class TextureItem(val material: Material, val customModelData: Int, val itemType: ItemType, val price : Int) {
    GUN_AK47(Material.BLACK_DYE, customModelData = 1, ItemType.AK47, 0),
    GUN_AWP(Material.BLACK_DYE, customModelData = 3, ItemType.AWP, 3000),
    GUN_SAIGA(Material.BLACK_DYE, customModelData = 5, ItemType.SAIGA, 3000),
    GUN_M870(Material.BLACK_DYE, customModelData = 7, ItemType.M870, 3000),
    GUN_MP5(Material.BLACK_DYE, customModelData = 9, ItemType.MP5, 3000),
    GUN_MOSIN(Material.BLACK_DYE, customModelData = 11, ItemType.MOSIN, 3000),
    GUN_DESERT_EAGLE(Material.BLACK_DYE, customModelData = 13, ItemType.DESERT_EAGLE, 1500),
    GUN_GLOCK(Material.BLACK_DYE, customModelData = 15, ItemType.GLOCK, 1500),
    MELEE_NATA(Material.PINK_DYE, customModelData = 1, ItemType.NATA, 1500),
    MELEE_HAMMER(Material.PINK_DYE, customModelData = 2, ItemType.HAMMER, 2000);
    fun getItem(): ItemStack {
        return item(material, customModelData = customModelData)
    }
    fun getItem(displayName: String, lore : ArrayList<String>): ItemStack {
        return item(material, customModelData = customModelData, displayName = displayName, lore = lore)
    }
    fun getItemWithPriceUnlock(itemTypes: MutableSet<ItemType>): ItemStack {
        val msg = if (itemTypes.contains(itemType)) {
            "${ChatColor.GREEN}${itemType}開放済み"
        }
        else {
            "${ChatColor.RED}${itemType}まだ開放していません"
        }
        return getItem(displayName = msg, lore = arrayListOf("${ChatColor.GOLD}値段: $price"))
    }
}