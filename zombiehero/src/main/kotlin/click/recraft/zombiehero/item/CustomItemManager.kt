package click.recraft.zombiehero.item

import org.bukkit.inventory.ItemStack
import java.util.*

interface CustomItemManager {
    val save: HashMap<UUID, CustomItem>

    fun register(uuid: UUID, customItem: CustomItem) {
        save[uuid] = customItem
    }

    fun getItem(itemStack: ItemStack?): CustomItem? {
        val meta = itemStack?.itemMeta ?: return null
        val uuid = try {
            UUID.fromString(meta.localizedName)
        } catch (e: IllegalArgumentException) {
            null
        }
        uuid ?: return null
        return save[uuid]
    }

    fun getItem(uuid: UUID): CustomItem? {
        return save[uuid]
    }

    fun unRegister(uuid: UUID) {
        save.remove(uuid)
    }
}