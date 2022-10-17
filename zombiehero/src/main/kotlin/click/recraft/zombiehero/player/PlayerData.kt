package click.recraft.zombiehero.player

import click.recraft.zombiehero.ZombieHero
import click.recraft.zombiehero.item.gun.Gun
import click.recraft.zombiehero.monster.api.MonsterType
import org.bukkit.entity.Player
import java.util.*
import kotlin.collections.HashMap

object PlayerData {
    private val monsterData: HashMap<UUID, MonsterType> = hashMapOf()
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
    fun Player.usingGun(): Gun? {
        val item = inventory.getItem(0)!!
        val customItem = ZombieHero.plugin.customItemFactory.getItem(item)
        if (customItem !is Gun) return null
        return customItem
    }
    fun Player.addAmmo(ammoNum: Int) {
        val gun = usingGun()!!
        gun.stats.totalArmo += ammoNum
        sendMessage("${org.bukkit.ChatColor.BOLD}メインウェポンの弾を${ammoNum}入手した")
    }
}