package click.recraft.zombiehero.item

import click.recraft.share.item
import click.recraft.zombiehero.gun.api.Reload
import click.recraft.zombiehero.gun.api.Shot
import click.recraft.zombiehero.item.gun.ShootManager
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.block.Action
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import java.util.*

class PlayerGun (
    material: Material,
    name: String,
    customModeValue: Int,
    manager: CustomItemManager,
    private val shootManager: ShootManager,
    private val reload: Reload,
    private val shot: Shot,
    private val walkSpeed: Float,
) : CustomItem (
    manager,
    item (
        material = material,
        localizedName = UUID.randomUUID().toString(),
        displayName = "${ChatColor.GOLD}$name",
        customModelData = customModeValue,
        lore = listOf(
            "reloadTime: ${reload.reloadTime}"
        )
    )
) {
    data class GunStats (
        var totalArmo:   Int,
        var currentArmo: Int,
        var maxArmo:     Int,
        var reloading:   Boolean,
        var gunName: String,
    ) {
    }
    private val stats = GunStats(
        reload.armo * 5,
        reload.armo,
        reload.armo,
        false,
        name
    )
    fun playerGiveItem(player: Player) {
        val item = createItemStack()
        if (!player.inventory.contains(item)) {
            player.inventory.addItem(createItemStack())
        }
        player.sendExperienceChange(1.0F, stats.totalArmo)
    }

    fun shootAction(player: Player): Boolean {
        if (stats.reloading) {
            return false
        }
        if (stats.currentArmo <= 0) {
            reload(player)
            return false
        }
        val passedTick = System.currentTimeMillis() - shot.lastShot
        if (passedTick < shot.rate.getMilliseconds()) {
            return false
        }
        shot.lastShot = System.currentTimeMillis()
        stats.currentArmo += -1
        if (stats.currentArmo != 0) {
            if (isSimilar(player.inventory.itemInMainHand)) {
                player.inventory.setItemInMainHand(createItemStack())
            }
        }
        shot.shoot(player, stats)
        return true
    }

    override fun createItemStack(): ItemStack {
        return super.createItemStack().apply {amount = stats.currentArmo}
    }

    private fun shoot(player: Player) {
        shootAction(player)
        shootManager.register(player, this)
    }

    fun reload(player: Player) {
        reload.reload(player, stats)
    }

    override fun inInvItemClick(clickType: ClickType?, player: Player?) {
    }

    override fun itemInteract(event: PlayerInteractEvent?, equipmentSlot: EquipmentSlot?) {
        val action = event!!.action
        val player = event.player
        if (action == Action.RIGHT_CLICK_BLOCK || action == Action.RIGHT_CLICK_AIR) {
            shoot(player)
        }
        if (action == Action.LEFT_CLICK_BLOCK || action == Action.LEFT_CLICK_AIR) {
            player.sendMessage("leftclick")
        }
    }
}