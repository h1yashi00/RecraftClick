package click.recraft.zombiehero

import org.bukkit.Bukkit
import org.bukkit.entity.Entity
import java.util.*

object EntityManager {
    private val save: MutableSet<UUID> = mutableSetOf()
    fun register(entity: Entity) {
        entity.uniqueId
        save.add(entity.uniqueId)
    }

    fun clear() {
        save.forEach {
            val entity = Bukkit.getEntity(it) ?: return@forEach
            entity.remove()
        }
        save.clear()
    }
}