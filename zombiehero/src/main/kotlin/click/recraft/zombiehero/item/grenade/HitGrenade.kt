package click.recraft.zombiehero.item.grenade

import click.recraft.share.item
import click.recraft.zombiehero.item.CustomItem
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.entity.ThrownPotion
import org.bukkit.event.block.Action
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.PotionMeta
import java.util.*


class HitGrenade (
    name: String
): CustomItem (
    item(
        Material.MAGENTA_DYE,
        localizedName = UUID.randomUUID().toString(),
        displayName = name,
        customModelData = 123840
    )
) {
    companion object {
        val saveProjectileEntity = mutableSetOf<UUID>()
    }
    override fun inInvItemClick(clickType: ClickType, player: Player) {
    }
    private fun makePotionItem(): ItemStack {
        val item = ItemStack(Material.SPLASH_POTION)
        val meta = item.itemMeta as PotionMeta
        meta.color = Color.ORANGE
        item.itemMeta = meta
        return item
    }

    override fun itemInteract(event: PlayerInteractEvent, equipmentSlot: EquipmentSlot) {
        val action = event.action
        if (!(action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK)) return
        val player = event.player
        val loc = player.eyeLocation
        val entity = loc.world!!.spawnEntity(loc, EntityType.SPLASH_POTION) as ThrownPotion
        entity.apply {
            item = makePotionItem()
            setBounce(false)
            velocity = loc.direction.multiply(1.0)
            saveProjectileEntity.add(uniqueId)
        }
    }
}