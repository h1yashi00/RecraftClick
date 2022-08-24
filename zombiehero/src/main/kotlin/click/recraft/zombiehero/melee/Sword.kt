package click.recraft.zombiehero.melee

import click.recraft.share.item
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

//open class Sword (
//    override val damage: Int,
//    override val attackSpeed: Int,
//    // player.walkingSpeed Base value is 0.2F
//    override val walkingSpeed: Float = 0.2F,
//    override val swapAttackTime: Double,
//    override val customModelValue: Int,
//    override val name: String,
//    ):SwordBase {
//    override fun getItem(time: Double): ItemStack {
//        val item = if (time == 0.0) {
//            item(material,  customModelData = customModelValue, displayName = "${ChatColor.GOLD}$name ${ChatColor.GOLD}R")
//        } else {
//            item(material,  customModelData = customModelValue, displayName = "${ChatColor.GOLD}$name ${ChatColor.WHITE}%.1f".format(time))
//        }
//        return item
//    }
//}