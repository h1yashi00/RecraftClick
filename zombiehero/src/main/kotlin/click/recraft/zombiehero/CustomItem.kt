package click.recraft.zombiehero

import click.recraft.share.item
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

interface CustomItem {
    val customModelValue: Int
    val material: Material
    fun getItem(displayName: String): ItemStack {
        return item(material, displayName = displayName, customModelData = customModelValue)
    }
    fun isItem(itemStack: ItemStack?): CustomItem? {
        itemStack ?: return null
        val meta = itemStack.itemMeta ?: return null
        if (!meta.hasCustomModelData()) return null
        if (meta.customModelData != customModelValue) return null
        return this
    }
}