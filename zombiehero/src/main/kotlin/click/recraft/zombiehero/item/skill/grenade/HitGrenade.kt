package click.recraft.zombiehero.item.skill.grenade

import click.recraft.share.item
import click.recraft.zombiehero.item.CustomItem
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.entity.ThrownPotion
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.player.PlayerInteractEvent
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

    override fun rightClick(event: PlayerInteractEvent) {
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

    override fun leftClick(event: PlayerInteractEvent) {
    }

    private fun makePotionItem(): ItemStack {
        val item = ItemStack(Material.SPLASH_POTION)
        val meta = item.itemMeta as PotionMeta
        meta.color = Color.ORANGE
        item.itemMeta = meta
        return item
    }
}