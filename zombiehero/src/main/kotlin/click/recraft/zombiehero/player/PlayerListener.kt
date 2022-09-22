package click.recraft.zombiehero.player

import click.recraft.zombiehero.MapObjectManager
import click.recraft.zombiehero.Util
import click.recraft.zombiehero.ZombieHero
import click.recraft.zombiehero.event.MonsterAttackPlayerEvent
import click.recraft.zombiehero.event.PlayerDeadPluginHealthEvent
import click.recraft.zombiehero.item.gun.Gun
import click.recraft.zombiehero.monster.api.Monster
import click.recraft.zombiehero.monster.api.MonsterManager
import click.recraft.zombiehero.player.PlayerData.isHeadShot
import org.bukkit.*
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent

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
    fun placeBlock(event: BlockPlaceEvent) {
        val player = event.player
        if (player.gameMode != GameMode.SURVIVAL) return
        val block = event.block
        MapObjectManager.register(block.location, Material.AIR, 20 * 4)
    }

    @EventHandler
    fun breakEvent(event: BlockBreakEvent) {
        val player = event.player
        if (player.gameMode != GameMode.SURVIVAL) {return}
        event.isCancelled = true
        val block = event.block
        if (block.type != Material.COAL_ORE) {return}

        val monster = MonsterManager.get(player)
        if (monster != null) return
        val item = player.inventory.getItem(0)!!
        val customItem = ZombieHero.plugin.customItemFactory.getItem(item) ?: return
        if (customItem  !is Gun) return

        val loc = block.location
        loc.world!!.spawnParticle(Particle.BLOCK_CRACK, loc, 10, block.type.createBlockData())
        val addArmo = customItem.stats.maxArmo / 3
        player.sendMessage("${ChatColor.BOLD}${customItem.stats.gunName}の弾を${addArmo}入手した")
        player.playSound(player, Sound.ENTITY_ITEM_PICKUP, 1f,1f)
        customItem.stats.totalArmo += addArmo
        MapObjectManager.register(block.location, block.type, 20 * 3)
        block.type = Material.AIR
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