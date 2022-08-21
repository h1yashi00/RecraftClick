package click.recraft.zombiehero.gun.base.subtype

import click.recraft.share.item
import click.recraft.zombiehero.gun.base.GunNoProjectile
import click.recraft.zombiehero.gun.base.RightClickAction
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class GunSubWeapon(
    material: Material,
    data: Short,
    customModelData: Int,
    fireRate: Long,
    name: String,
    knockBack: Int,
    damage: Double,
    maxArmo: Int,
    nameColor: ChatColor = ChatColor.GOLD,
    reloadTime: Long,
    private val maxSubArmo: Int,
    spread: Double,
    recoilY: Float,
    recoilZ: Float,
    bulletRange: Int,
): GunNoProjectile(
    material,
    data,
    customModelData,
    fireRate,
    name,
    knockBack,
    damage,
    maxArmo,
    nameColor,
    reloadTime,
    spread = spread,
    recoilY = recoilY,
    recoilZ = recoilZ,
    bulletRange = bulletRange
), RightClickAction {
    override fun getItem(armo: Int, reloading: Boolean): ItemStack {
        return item(material,
            displayName = "$nameColor$name${ChatColor.WHITE}●[$armo]► ▻ ◄ ◅[$maxSubArmo]"
        )
    }
    override fun rightClickAction(player: Player) {
    }
}