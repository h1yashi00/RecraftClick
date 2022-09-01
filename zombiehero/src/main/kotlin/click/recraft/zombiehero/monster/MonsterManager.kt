package click.recraft.zombiehero.monster

import click.recraft.zombiehero.Util
import click.recraft.zombiehero.ZombieHero
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.*

class MonsterManager {
    private val save: HashMap<UUID, Monster> = hashMapOf()
    init {
        val task = Util.createTask {
            save.values.iterator().forEach { monster ->
                monster.passOneSec()
            }
        }
        Bukkit.getScheduler().runTaskTimer(ZombieHero.plugin, task,20 * 5, 20)
    }

    fun register(monster: Monster) {
        save[monster.uniqueId] = monster
    }

    fun get(player: Player): Monster? {
        return save[player.uniqueId]
    }

    fun register(player: Player, monster: Monster) {
        save[player.uniqueId] = monster
    }
}