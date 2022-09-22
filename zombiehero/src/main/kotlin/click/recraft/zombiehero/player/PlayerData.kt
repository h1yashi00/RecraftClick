package click.recraft.zombiehero.player

import click.recraft.zombiehero.ZombieHero
import click.recraft.zombiehero.item.CustomItemFactory
import click.recraft.zombiehero.monster.api.MonsterType
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.*
import kotlin.collections.HashMap

object PlayerData {
    private val monsterData: HashMap<UUID, MonsterType> = hashMapOf()
    private val mainGunData: HashMap<UUID, CustomItemFactory.MainGunType> = hashMapOf()
    private val subGunData: HashMap<UUID, CustomItemFactory.SubGunType> = hashMapOf()
    private val swordData : HashMap<UUID, CustomItemFactory.SwordType> = hashMapOf()
    private val shooter: HashMap<UUID, Player> = hashMapOf()
    private val isHeadShot: HashMap<UUID, Int> = hashMapOf()
    private val playerSkillSpeed = HashMap<UUID, Int>()
    private val playerSKillHeadShot: MutableSet<UUID> = mutableSetOf()

    fun clear() {
        shooter.clear()
        isHeadShot.clear()
        playerSKillHeadShot.clear()
        playerSkillSpeed.clear()
    }
    fun Player.setSkillSpeed(speed: Int) {
        playerSkillSpeed[uniqueId] = speed
    }
    fun Player.removeSkillSpeed(){
        playerSkillSpeed.remove(uniqueId)
    }
    fun Player.getSkillSpeed(): Int? {
        return playerSkillSpeed[uniqueId]
    }
    fun Player.isContainsSkillSpeed(): Boolean {
        return playerSkillSpeed.contains(uniqueId)
    }

    fun Player.removePlayerSkillHeadShot() {
        playerSKillHeadShot.remove(uniqueId)
    }
    fun Player.setPlayerSkillHeadShot() {
        playerSKillHeadShot.add(uniqueId)
    }
    fun Player.isPlayerSkillHeadShot(): Boolean {
        return playerSKillHeadShot.contains(uniqueId)
    }

    fun Player.setShooter(player: Player) {
        shooter[uniqueId] = player
    }

    fun Player.shooter(): Player {
        return shooter[uniqueId] ?: Bukkit.getOnlinePlayers().first()
    }

    fun Player.removeHeadShot() {
        isHeadShot.remove(uniqueId)
    }

    fun Player.setHeadShot() {
        isHeadShot[uniqueId] = ZombieHero.plugin.getTime()
    }
    fun Player.isHeadShot() : Boolean {
        return isHeadShot[uniqueId] == ZombieHero.plugin.getTime()
    }
    fun Player.setMonsterType(monsterType: MonsterType) {
        monsterData[uniqueId] = monsterType
    }
    fun Player.monsterType(): MonsterType {
        return monsterData[uniqueId] ?: MonsterType.ZOMBIE
    }
    fun Player.mainGunType(): CustomItemFactory.MainGunType {
        return mainGunData[uniqueId] ?: CustomItemFactory.MainGunType.AK47
    }
    fun Player.setMainGunType(mainGunType: CustomItemFactory.MainGunType) {
        mainGunData[uniqueId] = mainGunType
    }
    fun Player.subGunType(): CustomItemFactory.SubGunType {
        return subGunData[uniqueId] ?: CustomItemFactory.SubGunType.DesertEagle
    }
    fun Player.setSubGunType(subGunType: CustomItemFactory.SubGunType) {
        subGunData[uniqueId] = subGunType
    }
    fun Player.setSword(sword: CustomItemFactory.SwordType) {
        swordData[uniqueId] = sword
    }
    fun Player.sword(): CustomItemFactory.SwordType {
        return swordData[uniqueId] ?: CustomItemFactory.SwordType.NORMAL_SWORD
    }
}