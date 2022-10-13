package click.recraft.share.protocol

import click.recraft.share.item
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
    MELEE_HAMMER(Material.PINK_DYE, customModelData = 2, ItemType.HAMMER);
    fun getItem(): ItemStack {
        return item(material, customModelData = customModelData)
    }
}