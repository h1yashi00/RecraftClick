package click.recraft.zombiehero

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.CreatureSpawnEvent

class MonsterSpawn: Listener {
    @EventHandler
    fun monsterSpawn(event: CreatureSpawnEvent) {
        if (event.spawnReason == CreatureSpawnEvent.SpawnReason.NATURAL) return
    }
}