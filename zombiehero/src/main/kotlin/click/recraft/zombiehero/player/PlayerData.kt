package click.recraft.zombiehero.player

import click.recraft.zombiehero.item.CustomItemFactory
import click.recraft.zombiehero.monster.api.MonsterType
import org.bukkit.entity.Player
import java.util.*
import kotlin.collections.HashMap

object PlayerData {
    private val monsterData: HashMap<UUID, MonsterType> = hashMapOf()
    private val mainGunData: HashMap<UUID, CustomItemFactory.MainGunType> = hashMapOf()
    private val subGunData: HashMap<UUID, CustomItemFactory.SubGunType> = hashMapOf()
    private val meleeData : HashMap<UUID, CustomItemFactory.MeleeType> = hashMapOf()
    private val grenadeData : HashMap<UUID, CustomItemFactory.GrenadeType> = hashMapOf()
    private val playerSkillSpeed = HashMap<UUID, Int>()
    private val playerSKillHeadShot: MutableSet<UUID> = mutableSetOf()

    fun clear() {
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
    fun Player.setMelee(melee: CustomItemFactory.MeleeType) {
        meleeData[uniqueId] = melee
    }
    fun Player.melee(): CustomItemFactory.MeleeType {
        return meleeData[uniqueId] ?: CustomItemFactory.MeleeType.NATA
    }
    fun Player.setGrenade(grenade: CustomItemFactory.GrenadeType) {
        grenadeData[uniqueId] = grenade
    }
    fun Player.grenade(): CustomItemFactory.GrenadeType {
        return grenadeData[uniqueId] ?: CustomItemFactory.GrenadeType.NormalGrenade
    }
}