package click.recraft.zombiehero.item

import click.recraft.share.item
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import java.util.*


// dont stack custom item
abstract class CustomItem(
    private val item: ItemStack
) {
    val unique: UUID
    init {
        val strUUID= item.itemMeta!!.localizedName
        unique = UUID.fromString(strUUID)!!
    }
    open fun createItemStack(): ItemStack {
        val meta = item.itemMeta!!
        return item(
            material = item.type,
            customModelData = meta.customModelData,
            displayName = meta.displayName,
            lore = meta.lore ?: listOf(),
            localizedName = unique.toString()
        )
    }

    abstract fun inInvItemClick(clickType: ClickType, player: Player)
    abstract fun rightClick(event: PlayerInteractEvent)
    abstract fun leftClick(event: PlayerInteractEvent)
    fun isDroppable(): Boolean {
        return false
    }
    fun isMovable(): Boolean {
        return false
    }
    fun isSwapable(): Boolean {
        return false
    }

    open fun isSimilar(itemStack: ItemStack?): Boolean {
        val meta = itemStack?.itemMeta ?: return false
        val itemMeta = itemStack.itemMeta!!
        if (!(meta.hasCustomModelData() && meta.hasLocalizedName())) {
            return false
        }
        if (!(itemMeta.customModelData == meta.customModelData && itemMeta.localizedName == meta.localizedName)) {
            return false
        }
        return true
    }
}