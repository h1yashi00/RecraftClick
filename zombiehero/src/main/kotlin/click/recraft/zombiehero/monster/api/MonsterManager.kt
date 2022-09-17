package click.recraft.zombiehero.monster.api

import click.recraft.zombiehero.Util
import click.recraft.zombiehero.ZombieHero
import click.recraft.zombiehero.monster.Skeleton
import click.recraft.zombiehero.monster.Zombie
import click.recraft.zombiehero.player.PlayerData.choosedMonster
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
    fun remove(player: Player) {
        save.remove(player.uniqueId)
    }
    fun get(player: Player): Monster? {
        return save[player.uniqueId]
    }

    fun contains(player: Player): Boolean {
        return save.containsKey(player.uniqueId)
    }

    private fun getMonsterConstructor(type: MonsterType, player: Player): Monster {
        return when(type) {
            MonsterType.ZOMBIE -> Zombie(player.uniqueId)
            MonsterType.SKELETON -> Skeleton(player.uniqueId)
        }
    }

    fun register(player: Player) {
        player.inventory.clear()
        val monster = getMonsterConstructor(player.choosedMonster(), player)
        remove(player)
        monster.initialize(player)
        save[monster.playerUUID] = monster
    }

    fun register(player: Player, type: MonsterType) {
        player.inventory.clear()
        val monster = getMonsterConstructor(type, player)
        remove(player)
        monster.initialize(player)
        save[monster.playerUUID] = monster
    }




    // 再帰的に候補者を選択
    private fun getEnemyCandidate(): Player {
        val candidate = Bukkit.getOnlinePlayers().random()
        val monster = get(candidate)
        if (monster != null) {
            return getEnemyCandidate()
        }
        return candidate
    }

    fun chooseRandomEnemyMonster() {
        val player = getEnemyCandidate()
        register(player)
    }
}