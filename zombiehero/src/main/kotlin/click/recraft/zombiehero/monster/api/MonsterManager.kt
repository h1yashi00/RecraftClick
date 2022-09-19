package click.recraft.zombiehero.monster.api

import click.recraft.zombiehero.Util
import click.recraft.zombiehero.ZombieHero
import click.recraft.zombiehero.monster.Skeleton
import click.recraft.zombiehero.monster.Zombie
import click.recraft.zombiehero.player.PlayerData.choosedMonster
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import java.util.*

object MonsterManager {
    private val save: HashMap<UUID, Monster> = hashMapOf()
    init {
        val task = Util.createTask {
            ZombieHero.plugin.importantTaskId.add(taskId)
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

    fun register(player: Player): Monster {
        player.inventory.clear()
        val monster = getMonsterConstructor(player.choosedMonster(), player)
        remove(player)
        monster.initialize(player)
        save[monster.playerUUID] = monster
        return monster
    }

    fun register(player: Player, type: MonsterType) {
        player.inventory.clear()
        val monster = getMonsterConstructor(type, player)
        remove(player)
        monster.initialize(player)
        save[monster.playerUUID] = monster
    }


    // 再帰的に候補者を選択

    fun chooseRandomEnemyMonster() {
        if (humans().size == 0) {
            println("${ChatColor.RED}NO HUMANS!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! ")
            return
        }
        val human = humans().random()
        register(human)
    }

    private fun humans(): MutableSet<Player> {
        val humans: MutableSet<Player> = mutableSetOf()
        Bukkit.getOnlinePlayers().forEach {
            val monster = get(it)
            if (monster == null) {
                humans.add(it)
            }
        }
        return humans
    }
    private fun monsters(): MutableCollection<Monster> {
        return save.values
    }
    fun humansNum(): Int {
        return humans().size
    }

    fun aliveMonsters(): Int {
        var alive = 0
        monsters().forEach { monster ->
            if (!monster.isDead) {
                alive += 1
            }
        }
        return alive
    }

    fun clear() {
        save.clear()
    }
}