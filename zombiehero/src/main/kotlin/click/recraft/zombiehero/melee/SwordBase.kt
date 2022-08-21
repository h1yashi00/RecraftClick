package click.recraft.zombiehero.melee

import click.recraft.zombiehero.CustomItem
import org.bukkit.ChatColor
import org.bukkit.inventory.ItemStack

interface SwordBase: CustomItem {
    val damage: Int
    val attackSpeed: Int
    val walkingSpeed: Float
    val swapAttackTime: Double
    val name: String
    override fun isItem(itemStack: ItemStack?): SwordBase? {
        val item = super.isItem(itemStack)
        if (item !is SwordBase) return null
        return this
    }
    fun getItem(): ItemStack {
        return getItem(swapAttackTime)
    }

    fun getItem(time: Double): ItemStack {
        val displayName = if (time == 0.0) {
            "${ChatColor.GOLD}$name ${ChatColor.GOLD}R"
        } else {
            "${ChatColor.GOLD}$name ${ChatColor.WHITE}$time"
        }
        return getItem(displayName)
    }
    // default value

    fun isReady(itemStack: ItemStack?): Boolean {
        itemStack ?: return false
        val meta = itemStack.itemMeta ?: return false
        return meta.displayName.contains("${ChatColor.GOLD}R")
    }
}