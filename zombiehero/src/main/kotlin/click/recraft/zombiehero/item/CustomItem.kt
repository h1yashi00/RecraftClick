package click.recraft.zombiehero.item

import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import java.util.*


// dont stack custom item
interface CustomItem {
    val uuid: UUID
    val manager: CustomItemManager
    val customModelData: Int
    val itemStack: ItemStack

    fun initialize() {
        val item = ItemStack(itemStack.type)
        val meta = item.itemMeta!!
        meta.setCustomModelData(customModelData)
        meta.setLocalizedName(uuid.toString())
        itemStack.itemMeta = meta
        manager.register(uuid, this)
    }

    fun inInvItemClick(clickType: ClickType?, player: Player?)
    fun itemInteract(event: PlayerInteractEvent?, equipmentSlot: EquipmentSlot?)
    fun isDroppable(): Boolean {
        return false
    }
    fun isMovable(): Boolean {
        return false
    }
    fun isSwapable(): Boolean {
        return false
    }

    fun isSimilar(itemStack: ItemStack?): Boolean {
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