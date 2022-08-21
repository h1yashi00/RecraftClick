package click.recraft.zombiehero.gun.base.scope

import click.recraft.zombiehero.ZombieHero
import click.recraft.zombiehero.gun.base.GunNoProjectile
import click.recraft.zombiehero.gun.base.RightClickAction
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitTask
import java.util.*

open class GunScope(
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
    private val magnification: ScopeMagnification,
    spread: Double,
    recoilY: Float,
    recoilZ: Float,
    bulletRange: Int
): GunNoProjectile (
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
    recoilZ = recoilZ,
    recoilY = recoilY,
    bulletRange = bulletRange
), RightClickAction {
    private class GunScopePlayerData
    init {
        val task: (BukkitTask) -> Unit = {
            saved.iterator().forEach {
                val uuid = it.key
                val player = Bukkit.getPlayer(uuid)
                if (player == null) {
                    saved.remove(uuid)
                    return@forEach
                }
                // プレイヤーの持っているアイテムが今のアイテムと等しいか確認
                // スコープの鈍化をなくして戻る
                val meta = player.inventory.itemInMainHand.itemMeta
                if (meta == null) {
                    saved.remove(uuid)
                    return@forEach
                }
                if (!meta.hasCustomModelData()) {
                    saved.remove(uuid)
                    return@forEach
                }
                val currentItemCustomModelData = meta.customModelData
                if (currentItemCustomModelData != customModelData) {
                    player.removePotionEffect(magnification.potionEffect.type)
                    saved.remove(uuid)
                    return@forEach
                }
                // エフェクトを与え続ける
                player.addPotionEffect(magnification.potionEffect)
            }
            Bukkit.getOnlinePlayers().forEach {player ->
                val gun = isGun(player.inventory.itemInMainHand) ?: return@forEach
                if (gun !is GunScope) return@forEach
                val loc = player.location
                loc.pitch = 0F
                loc.yaw   = 0F
                moveCheckerSaved[player.uniqueId] = loc
            }
        }
        Bukkit.getScheduler().runTaskTimer(ZombieHero.plugin, task, 1,20)
    }
    // saved内に入っていると定期的に鈍化のポーションエフェクトを与え続ける
    private val saved = hashMapOf<UUID, GunScopePlayerData>()
    private var moveChecker: Boolean = false
    private val moveCheckerSaved = hashMapOf<UUID, Location>()
    private fun dynamicSpread(): Double {
        if (moveChecker) {
            return 0.3
        }
        return 0.0
    }
    override val spread: Double
        get() = dynamicSpread()
    override fun gunAction(player: Player) {
        if (saved.containsKey(player.uniqueId)) {
            moveChecker = false
        }
        else {
            val loc = moveCheckerSaved[player.uniqueId]
            val currentLoc = player.location
            currentLoc.pitch = 0F
            currentLoc.yaw   = 0F
            moveChecker = currentLoc != loc
        }
        super.gunAction(player)
    }
    override fun rightClickAction(player: Player) {
        player.addPotionEffect(magnification.potionEffect)
        if (saved.containsKey(player.uniqueId)) {
            val data = saved[player.uniqueId]!!
            player.removePotionEffect(magnification.potionEffect.type)
            saved.remove(player.uniqueId)
        }
        else {
            saved[player.uniqueId] = GunScopePlayerData()
        }
    }
}