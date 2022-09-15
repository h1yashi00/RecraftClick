package click.recraft.zombiehero.item.gun

import click.recraft.share.item
import click.recraft.zombiehero.ZombieHero
import click.recraft.zombiehero.gun.api.GameSound
import click.recraft.zombiehero.gun.api.Reload
import click.recraft.zombiehero.gun.api.ShootManager
import click.recraft.zombiehero.gun.api.Shot
import click.recraft.zombiehero.item.CustomItem
import click.recraft.zombiehero.player.WalkSpeed
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import java.util.*

open class Gun (
    name: String,
    customModeValue: Int,
    private val shootManager: ShootManager,
    private val reload: Reload,
    private val shot: Shot,
    override val walkSpeed: Int,
    val reloadSound: GameSound,
    val reloadFinishSound: GameSound,
    val shotSound: GameSound,
    val recoilX: Float,
    val recoilY: Float,
) : WalkSpeed, CustomItem(
    item (
        material = Material.BLACK_DYE,
        localizedName = UUID.randomUUID().toString(),
        displayName = "${ChatColor.GOLD}$name",
        customModelData = customModeValue,
        lore = listOf(
            "${ChatColor.AQUA}弾数: ${reload.armo}",
            "${ChatColor.AQUA}リロード時間: ${reload.reloadTime.getMilliseconds() / 1000}秒",
            "${ChatColor.AQUA}正確さ: ${shot.accuracy.value}",
            "${ChatColor.AQUA}レート: ${shot.rate.getMilliseconds() / 1000}秒",
            "${ChatColor.AQUA}ダメージ: ${shot.damage}",
            "${ChatColor.AQUA}発射される弾数: ${shot.shootAmmo}",
            "${ChatColor.AQUA}ダメージ/秒: ${shot.damage * shot.shootAmmo / (shot.rate.getMilliseconds().toDouble() / 1000.0)}",
        )
    )
) {
    override fun createItemStack(): ItemStack {
        return if (scope) {
            val itemStack = super.createItemStack()
            val meta = itemStack.itemMeta!!
            val reloadCustomItemMeta = meta.customModelData + 1
            meta.setCustomModelData(reloadCustomItemMeta)
            itemStack.itemMeta = meta
            itemStack
        } else {
            super.createItemStack()
        }
    }
    data class GunStats (
        var totalArmo:   Int,
        var currentArmo: Int,
        var maxArmo:     Int,
        var gunName: String,
    )
    override fun isSimilar(itemStack: ItemStack?): Boolean {
        itemStack ?: return false
        val meta = itemStack.itemMeta ?: return false
        if (!meta.hasLocalizedName()) return false
        if (meta.localizedName != unique.toString()) { return false }
        return true
    }

    val stats = GunStats(
        reload.armo * 5,
        reload.armo,
        reload.armo,
        name
    )
    fun isRelaoding(): Boolean {
        return reload.reloadManager.contains(this)
    }
    fun cancelReload() {
        reload.reloadManager.remove(this)
    }

    fun playerGiveItem(player: Player) {
        val item = createItemStack()
        if (!player.inventory.contains(item)) {
            player.inventory.addItem(createItemStack())
        }
        player.sendExperienceChange(1.0F, stats.totalArmo)
    }

    open fun getReloadTime(): Int {
        return reload.reloadTime.tick
    }

    fun shootAction(player: Player) {
        if (stats.currentArmo <= 0) {
            reload(player)
            return
        }
        return shot.shootAction(player, this)
    }

    private fun shoot(player: Player) {
        shootAction(player)
        shootManager.register(player, this)
    }

    private fun updateGun(player: Player) {
        val item = player.inventory.itemInMainHand
        player.inventory.remove(item)
        player.inventory.setItemInMainHand(createItemStack())
    }

    fun reload(player: Player) {
        if (scope) {
            scope(player)
        }
        reload.reload(player, this)
    }

    override fun inInvItemClick(clickType: ClickType, player: Player) {
    }

    private var scope = false
    fun isScoping(): Boolean {
        return scope
    }

    private var qDropCheck = 0
    // qドロップする際に､InteractEventが発生するようになってしまったので､
    // それを回避するためのもの｡ interact と dropイベントが全く同じタイミングで発生した場合は､回避する
    fun isQDrop(): Boolean {
        val now = ZombieHero.plugin.getTime()
        // dropItemから1,2millionSecか遅れて､動作する場合があるのでそれをチェックする
        if (qDropCheck == now) {
            return true
        }
        qDropCheck = now
        return false
    }
    private val effect = PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 5)

    fun scope(player: Player) {
        if (isQDrop()) {
            return
        }
        if (scope) {
            scope = false
            player.removePotionEffect(effect.type)
            val item = player.inventory.itemInMainHand
            player.inventory.removeItem(item)
        } else {
            player.addPotionEffect(effect)
            scope = true
        }
        updateGun(player)
    }

    override fun rightClick(event: PlayerInteractEvent) {
        val player = event.player
        event.isCancelled = true
        shoot(player)
    }

    override fun leftClick(event: PlayerInteractEvent) {
        val player = event.player
        event.isCancelled = true
        scope(player)
    }
}