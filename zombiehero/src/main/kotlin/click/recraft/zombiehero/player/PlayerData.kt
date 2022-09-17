package click.recraft.zombiehero.player

import click.recraft.zombiehero.item.CustomItemFactory
import click.recraft.zombiehero.monster.api.MonsterType
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.*
import kotlin.collections.HashMap

object PlayerData {
    private val monsterData: HashMap<UUID, MonsterType> = hashMapOf()
    private val gunData: HashMap<UUID, CustomItemFactory.GunType> = hashMapOf()
    private val shooter: HashMap<UUID, Player> = hashMapOf()

    fun Player.setShooter(player: Player) {
        shooter[uniqueId] = player
    }

    fun Player.shooter(): Player {
        return shooter[uniqueId] ?: Bukkit.getOnlinePlayers().first()
    }
    fun Player.choosedMonster(): MonsterType {
        return monsterData[uniqueId] ?: MonsterType.ZOMBIE
    }
    fun Player.gun(): CustomItemFactory.GunType {
        return gunData[uniqueId] ?: CustomItemFactory.GunType.AK47
    }
}