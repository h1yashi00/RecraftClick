package click.recraft.zombiehero

import org.bukkit.Location
import org.bukkit.World

class GameWorld(
    val world: World
) {
    private val spawns : List<Location> = listOf(
        Location(world, -13.0, 0.0, 81.0),
    )
    fun randomSpawn(): Location {
        return spawns.random().clone().apply {y+=2}
    }
}