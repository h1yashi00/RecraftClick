package click.recraft.zombiehero.player

import click.recraft.share.database.PlayerManager
import click.recraft.share.extension.runTaskLater
import click.recraft.share.item
import click.recraft.zombiehero.*
import click.recraft.zombiehero.chat.ZombieHeroChatIcon
import click.recraft.zombiehero.event.*
import click.recraft.zombiehero.item.CustomItem
import click.recraft.zombiehero.item.gun.Gun
import click.recraft.zombiehero.item.melee.Melee
import click.recraft.zombiehero.monster.api.Monster
import click.recraft.zombiehero.monster.api.MonsterManager
import org.bukkit.*
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.player.AsyncPlayerChatEvent
import java.util.*
import kotlin.collections.HashMap

class PlayerListener: Listener {
    private fun reviveMonster(monster: Monster) {
        val player = Bukkit.getPlayer(monster.playerUUID) ?: return
        player.gameMode = GameMode.SPECTATOR
        runTaskLater(20 * 10) {
            player.gameMode = GameMode.SURVIVAL
            Bukkit.getOnlinePlayers().forEach { it.playSound(it, Sound.ENTITY_WITHER_SPAWN, 1f,0.5f) }
            player.teleport(ZombieHero.plugin.gameManager.world.randomSpawn())
        }
    }

    private fun headShotLog(item: CustomItem?, isHeadShot: Boolean): String {
        val icon = when (item) {
            is Melee -> {ZombieHeroChatIcon.knifeKill}
            is Gun -> {ZombieHeroChatIcon.gun}
            else -> ""
        }
        return if (isHeadShot) {
            "${icon}${ZombieHeroChatIcon.headshotBullet}${ZombieHeroChatIcon.headshotHead}"
        }
        else {
            icon
        }
    }
    private fun Player.coloredName(): String {
        MonsterManager.get(this) ?: return "${ChatColor.WHITE}${name}${ChatColor.GRAY}"
        return "${ChatColor.GREEN}${name}${ChatColor.GRAY}"
    }

    @EventHandler
    fun playerChat(event: AsyncPlayerChatEvent) {
        val player = event.player
        val msg = event.message
        event.isCancelled = true
        Bukkit.broadcastMessage("${player.coloredName()} ${ChatColor.BOLD} >> ${ChatColor.GRAY} $msg")
    }
    @EventHandler
    fun deathMonster(event: PlayerDeadPluginHealthEvent) {
        val victim = event.victim
        val attacker = if (event.attacker !is Player) return else {event.attacker}
        val world = victim.world
        val location = victim.location
        var monster = MonsterManager.get(victim)
        val victimIsHuman = monster == null
        val deathMsg = if (victimIsHuman) {
            "${attacker.coloredName()} ${ZombieHeroChatIcon.zombieAttack} ${victim.coloredName()}"
        } else {
            "${attacker.coloredName()} ${headShotLog(event.customItem ,event.isHeadShot)} ${victim.coloredName()}"
        }

        Bukkit.broadcastMessage(deathMsg)


        if (victimIsHuman) {
            PlayerManager.killHuman(attacker)
            // 人間が感染させられた
            monster = MonsterManager.register(victim)
            world.playSound(victim.location, "minecraft:stab_knife_body", 1f,0.7f)
            if (ZombieHero.plugin.gameManager.checkGameCondition()) return
            world.playSound(victim.location, "minecraft:man_shout", 0.5f,1f)
        }
        else {
            // ゾンビが死んだ
            if (monster == null) return
            val type = when (event.customItem) {
                is Melee -> PlayerManager.WeaponType.MELEE
                is Gun   -> PlayerManager.WeaponType.GUN
                else -> PlayerManager.WeaponType.MELEE
            }

            PlayerManager.killZombie(attacker, type)

            world.playSound(location, monster.deathSound, 2f,0.1f)
            event.reviveHp = monster.maxHealth
            // 復活できない
            if (event.isHeadShot) {
                monster.isDead = true
                victim.sendTitle("", "ラストショットがヘッドショットだったため復活できません", 20,20 * 3,20)
                victim.gameMode = GameMode.SPECTATOR
                ZombieHero.plugin.gameManager.checkGameCondition()
                return
            }
        }
        // ヘッドショットされなければ､どちらも復活する
        reviveMonster(monster)
    }

    @EventHandler
    fun hitBullet(event: BulletHitBlock) {
        val block = event.block
        if (block.type == Material.GLASS) {
            event.player.breakBlock(block)
        }
    }

    @EventHandler
    fun grenade(event: GrenadeExplodeEvent) {
        val grenade = event.grenade
        val player = event.player
        val damageEntities: HashMap<UUID, Double> = hashMapOf()
        event.damagedEntity.iterator().forEach { (uuid, damage) ->
            damageEntities[uuid] = damage * grenade.damageMultiplier
        }
        event.blockList.forEach { block ->
            if (block.type == Material.GLASS) {
                player.breakBlock(block)
            }
        }
    }

    @EventHandler
    fun placeBlock(event: BlockPlaceEvent) {
        val player = event.player
        if (player.gameMode != GameMode.SURVIVAL) return
        val block = event.block
        if (block.type == Material.GLASS) {
            val slot = event.hand
            player.inventory.setItem(slot, item(Material.GLASS))
        }
        MapObjectManager.placedBlock(block)
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun breakEvent(event: BlockBreakEvent) {
        val player = event.player
        if (player.gameMode != GameMode.SURVIVAL) return
        event.isCancelled = true
        val block = event.block
        if (!MapObjectManager.containsPlaced(block)) {
            return
        }
        MapObjectManager.remove(block)
        block.type = Material.AIR
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = false)
    fun humanBreakBlock(event: BlockBreakEvent) {
        val player = event.player
        if (player.gameMode != GameMode.SURVIVAL) {return}
        val block = event.block

        val monster = MonsterManager.get(player)
        if (monster != null) return
        val gameBlock = GameBlockBreak.get(block) ?: return
        event.isCancelled = true
        gameBlock.function(block, player)
    }
}