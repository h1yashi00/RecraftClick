package click.recraft.zombiehero.gun.base

import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.util.*


interface GunBase {
    // 発射する際に､レートをチェックして､レートよりも低い場合は何もせずに
    // 高かった場合は撃てるのでgunActionで処理する
    fun shoot(player: Player) {
        val pastFiredTick = fireRateHandle[player.uniqueId]
        if (pastFiredTick == null || System.currentTimeMillis() - pastFiredTick >= fireRate) {
            gunAction(player)
            fireRateHandle[player.uniqueId] = System.currentTimeMillis()
        }
    }
    fun isGun(itemStack: ItemStack?): GunBase? {
        if (itemStack == null) return null
        val meta = if (!itemStack.hasItemMeta()) return null else {itemStack.itemMeta} ?: return null
        val data = if (!meta.hasCustomModelData()) return null else meta.customModelData
        if (customModelData != data) {
            return null
        }
        return this
    }
    fun getCurrentGunStats(itemStack: ItemStack): GunStats {
        val meta = if (itemStack.hasItemMeta()) itemStack.itemMeta ?: return GunStats(this,0,0,false) else return GunStats(this,0,0,true)
        val statsString = meta.displayName.split(" ")
        val reloading = statsString[1][0] == '▫'
        val currentArmo = Integer.valueOf(statsString.last().split("[").last().split("]").first())!!
        return GunStats(this,currentArmo, maxArmo, reloading)
    }
    // Projectileエンティティを発射したり､
    // Projectileを発射しない場合は､相手が直線状にいるかどうかをチェックして
    // いた場合にはダメージを加えるような処理をする場所
    // 撃てる状態である場合にここは呼ばれる
    fun gunAction(player: Player)
    fun getItem(armo: Int = maxArmo, reloading: Boolean = false): ItemStack
    fun decArmo(gunStats: GunStats, itemInMainHand: ItemStack): ItemStack?

    val walkingSpeed: Float
    val material: Material
    val data: Short
    val customModelData: Int
    val name: String
    val maxArmo: Int
    val nameColor: ChatColor
    val reloadTime: Long
    val spread: Double
    val shootArmoNum: Int
    val reloadOneBullet: Boolean

    // 打てる速度
    val fireRate: Long
    //  hashMapOf()でシンプルに初期化してください
    val fireRateHandle: HashMap<UUID, Long>
    val knockBack: Int
    val damage: Double
    val recoilY: Float
    val recoilZ: Float
    val bulletRange: Int
}