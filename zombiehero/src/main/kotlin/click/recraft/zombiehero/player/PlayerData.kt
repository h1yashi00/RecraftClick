package click.recraft.zombiehero.player

import click.recraft.zombiehero.item.CustomItemFactory
import click.recraft.zombiehero.monster.api.MonsterType
import org.bukkit.entity.Player
import java.util.*
import kotlin.collections.HashMap

object PlayerData {
    private val monsterData: HashMap<UUID, MonsterType> = hashMapOf()
    private val gunData: HashMap<UUID, CustomItemFactory.GunType> = hashMapOf()
    fun Player.choosedMonster(): MonsterType {
        return monsterData[uniqueId] ?: MonsterType.ZOMBIE
    }
    fun Player.gun(): CustomItemFactory.GunType {
        return gunData[uniqueId] ?: CustomItemFactory.GunType.AK47
    }
}