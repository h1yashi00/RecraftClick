package click.recraft.zombiehero

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.entity.Entity
import java.util.*

object SpawnedEntityManager {
    private val saveEntity: MutableSet<UUID> = mutableSetOf()
    private val saveBlock: HashMap<Block, UUID> = hashMapOf()
    fun register(entity: Entity) {
        entity.uniqueId
        saveEntity.add(entity.uniqueId)
    }

    fun clear() {
        saveEntity.forEach {
            val entity = Bukkit.getEntity(it) ?: return@forEach
            entity.remove()
        }
        saveEntity.clear()

        saveBlock.forEach {(block, _) ->
            block.type = Material.AIR
        }
    }

}