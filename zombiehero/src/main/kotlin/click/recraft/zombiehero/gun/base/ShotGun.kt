package click.recraft.zombiehero.gun.base

import org.bukkit.ChatColor
import org.bukkit.Material

open class ShotGun (
    material: Material,
    data: Short = 0,
    customModelValue: Int,
    fireRate: Long,
    name: String,
    knockBack: Int,
    damage: Double,
    maxArmo: Int,
    nameColor: ChatColor = ChatColor.GOLD,
    reloadTime: Long,
    spread: Double,
    recoilY: Float,
    recoilZ: Float,
    bulletRange: Int,
    shootArmoNum: Int
) : GunNoProjectile (
    material,
    data,
    fireRate,
    name,
    knockBack,
    damage,
    maxArmo,
    nameColor,
    reloadTime,
    spread,
    customModelValue = customModelValue,
    recoilY = recoilY,
    recoilZ = recoilZ,
    bulletRange = bulletRange,
    shootArmoNum = shootArmoNum
) {
}