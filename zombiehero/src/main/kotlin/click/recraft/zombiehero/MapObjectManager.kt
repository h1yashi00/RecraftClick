package click.recraft.zombiehero

import click.recraft.zombiehero.task.OneTickTimerTask
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.block.data.BlockData
import org.bukkit.entity.Entity
import java.util.*
import kotlin.collections.HashMap

object MapObjectManager : OneTickTimerTask {
    private val saveEntity: MutableSet<UUID> = mutableSetOf()
    private val placedBlocks : MutableSet<Location> = mutableSetOf()
    private val mapBlocks : HashMap<Location, BlockData> = hashMapOf()
    private val resourceBlocks: HashMap<Location, ResourceBlock> = hashMapOf()

    fun placedBlock(entity: Entity) {
        entity.uniqueId
        saveEntity.add(entity.uniqueId)
    }

    fun placedBlock(block: Block) {
        placedBlocks.add(block.location)
    }
    fun mapBlock(block: Block) {
        mapBlocks[block.location] = block.blockData
    }

    private data class ResourceBlock(
        val blockData: BlockData,
        val location: Location,
        var tick: Int
    )
    fun resourceBlock(block: Block, tick: Int) {
        resourceBlocks[block.location] = ResourceBlock(block.blockData, block.location, tick)
    }

    fun containsPlaced(block: Block): Boolean { return placedBlocks.contains(block.location) }
    fun remove(block: Block) {
        placedBlocks.remove(block.location)
    }

    fun clear() {
        saveEntity.forEach {
            val entity = Bukkit.getEntity(it) ?: return@forEach
            entity.remove()
        }
        saveEntity.clear()

        placedBlocks.iterator().forEach {
            it.block.type = Material.AIR
        }
        placedBlocks.clear()

        mapBlocks.iterator().forEach { (loc, data) ->
            loc.block.setBlockData(data, false)
        }
        mapBlocks.clear()

        resourceBlocks.iterator().forEach {(loc, data) ->
            loc.block.setBlockData(data.blockData, false)
        }
        resourceBlocks.clear()
    }

    override fun loopEveryOneTick() {
        resourceBlocks.iterator().forEach {(loc, data) ->
            data.tick += -1
            if (data.tick <= 0) {
                loc.block.setBlockData(data.blockData, false)
            }
        }
    }
}