package click.recraft.zombiehero.player

import click.recraft.share.item
import click.recraft.zombiehero.*
import click.recraft.zombiehero.event.BulletHitBlock
import click.recraft.zombiehero.event.GrenadeExplodeEvent
import click.recraft.zombiehero.event.MonsterAttackPlayerEvent
import click.recraft.zombiehero.event.PlayerDeadPluginHealthEvent
import click.recraft.zombiehero.monster.api.Monster
import click.recraft.zombiehero.monster.api.MonsterManager
import click.recraft.zombiehero.player.PlayerData.isHeadShot
import org.bukkit.*
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import java.util.*
import kotlin.collections.HashMap

class PlayerListener: Listener {
    private fun reviveMonster(monster: Monster) {
        val player = Bukkit.getPlayer(monster.playerUUID) ?: return
        player.gameMode = GameMode.SPECTATOR
        val task = Util.createTask {
            player.gameMode = GameMode.SURVIVAL
            Bukkit.getOnlinePlayers().forEach { it.playSound(it, Sound.ENTITY_WITHER_SPAWN, 1f,0.5f) }
            player.teleport(ZombieHero.plugin.gameManager.world.randomSpawn())
        }
        Bukkit.getScheduler().runTaskLater(ZombieHero.plugin, task, 20 * 10)
    }
    @EventHandler
    fun deathMonster(event: PlayerDeadPluginHealthEvent) {
        val player = event.victim
        val world = player.world
        val location = player.location
        val monster = MonsterManager.get(player) ?: return
        world.playSound(location, monster.deathSound, 2f,0.1f)
        event.reviveHp = monster.maxHealth

        // 復活できない
        if (player.isHeadShot()) {
            monster.isDead = true
            player.sendTitle("", "ラストショットがヘッドショットだったため復活できません", 20,20 * 3,20)
            player.gameMode = GameMode.SPECTATOR
            ZombieHero.plugin.gameManager.checkGameCondition()
            return
        }
        reviveMonster(monster)
    }

    @EventHandler
    fun deathHuman(event: PlayerDeadPluginHealthEvent) {
        val player = event.victim
        var monster = MonsterManager.get(player)
        if (monster != null) return
        // human
        monster = MonsterManager.register(player)
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
        if (MapObjectManager.containsPlaced(block)) {
            MapObjectManager.remove(block)
        }
        else {
            MapObjectManager.mapBlock(block)
        }
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

    @EventHandler
    fun monster(event: MonsterAttackPlayerEvent) {
        val victim = event.victim
        val world = victim.world
        world.playSound(victim.location, "minecraft:stab_knife_body", 1f,0.7f)
        if (ZombieHero.plugin.gameManager.checkGameCondition()) return
        world.playSound(victim.location, "minecraft:man_shout", 0.5f,1f)
    }
}