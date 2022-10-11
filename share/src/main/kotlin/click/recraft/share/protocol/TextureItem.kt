package click.recraft.share.protocol

import click.recraft.share.item
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

enum class TextureItem(val material: Material, val customModelData: Int) {
    GUN_AK47(Material.BLACK_DYE, customModelData = 1),
    GUN_AWP(Material.BLACK_DYE, customModelData = 3),
    GUN_SAIGA(Material.BLACK_DYE, customModelData = 5),
    GUN_M870(Material.BLACK_DYE, customModelData = 7),
    GUN_MP5(Material.BLACK_DYE, customModelData = 9),
    GUN_MOSIN(Material.BLACK_DYE, customModelData = 11),
    GUN_DESERT_EAGLE(Material.BLACK_DYE, customModelData = 13),
    GUN_GLOCK(Material.BLACK_DYE, customModelData = 15),
    MELEE_NATA(Material.PINK_DYE, customModelData = 1),
    MELEE_HAMMER(Material.PINK_DYE, customModelData = 2);
    fun getItem(): ItemStack {
        return item(material, customModelData = customModelData)
    }
}