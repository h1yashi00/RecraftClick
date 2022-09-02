package click.recraft.zombiehero

import click.recraft.zombiehero.item.CustomItemFactory
import click.recraft.zombiehero.monster.Skeleton
import org.bukkit.Bukkit
import org.bukkit.entity.EntityType
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractAtEntityEvent
import org.bukkit.event.player.PlayerJoinEvent

class PlayerJoin: Listener {
    @EventHandler
    fun playerInteract(event: PlayerInteractAtEntityEvent) {
        if (event.rightClicked.type != EntityType.ARMOR_STAND) return
        event.isCancelled = true
    }
    @EventHandler
    fun joinPlayer(event: PlayerJoinEvent) {
        val player = event.player
        player.foodLevel = 20
        player.inventory.clear()
        val monster = Skeleton(player.uniqueId)
        monster.initialize(player)
        ZombieHero.plugin.monsterManager.register(player, monster)
        val factory = ZombieHero.plugin.customItemFactory
        val gun = factory.createGun(CustomItemFactory.GunType.AK47)
        val shotGun = factory.createGun(CustomItemFactory.GunType.SHOTGUN)
        val task = Util.createTask {
            player.inventory.addItem(gun.createItemStack())
            player.inventory.addItem(shotGun.createItemStack())
        }
        Bukkit.getScheduler().runTaskLater(ZombieHero.plugin, task, 1)
    }
}