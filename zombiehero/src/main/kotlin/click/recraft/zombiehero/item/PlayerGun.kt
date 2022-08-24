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

class PlayerGun(
    override val manager: CustomItemManager,
    override val customModelData: Int,
    private val shootManager: ShootManager,
    private val name: String,
    private val reload: Reload,
    private val shot: Shot,
    private val walkSpeed: Float,
) : CustomItem {
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

    override val uuid: UUID = UUID.randomUUID()
    override val itemStack: ItemStack =
        item(
            Material.STICK,
            displayName = "${ChatColor.GOLD}$name",
            lore = arrayOf(
                "リロード: ${reload.reloadTime}"
            )
        )
    fun shootAction(player: Player) {
        shot.shoot(player, stats)
    }

    private fun shoot(player: Player) {
        shootManager.register(player, this)
    }

    fun reload(player: Player) {
        reload.reload(player, stats)
    }

    override fun isDroppable(): Boolean {
        return super.isDroppable()
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