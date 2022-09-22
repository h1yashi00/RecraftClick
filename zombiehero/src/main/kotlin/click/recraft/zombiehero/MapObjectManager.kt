package click.recraft.zombiehero

import click.recraft.zombiehero.task.OneTickTimerTask
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.entity.Entity
import java.util.*

object MapObjectManager: OneTickTimerTask {
    private val saveEntity: MutableSet<UUID> = mutableSetOf()
    private val saveBlock: HashMap<Block, UUID> = hashMapOf()
    private val brokenBlockSave: MutableSet<BlockType> = mutableSetOf()
    private class BlockType (
        val location: Location,
        val type: Material,
        var tick: Int
    )

    fun register(entity: Entity) {
        entity.uniqueId
        saveEntity.add(entity.uniqueId)
    }

    fun register(location: Location, type: Material, restoreTick : Int) {
        val blockType = BlockType(location, type, restoreTick)
        brokenBlockSave.add(blockType)
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
    override fun loopEveryOneTick() {
        val removeItems = arrayListOf<BlockType>()
        brokenBlockSave.iterator().forEach {
            val tick = it.tick - 1
            if (tick <= 0) {
                it.location.block.type = it.type
                removeItems.add(it)
                return@forEach
            }
            it.tick = tick
        }
        removeItems.forEach { brokenBlockSave.remove(it) }
    }
}