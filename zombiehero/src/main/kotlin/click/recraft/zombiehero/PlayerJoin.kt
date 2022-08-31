package click.recraft.zombiehero

import click.recraft.zombiehero.item.CustomItemFactory
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
        player.walkSpeed = 0.20F
        player.inventory.clear()
        val factory = CustomItemFactory()
        val gun = factory.createGun(CustomItemFactory.GunType.AK47)
        val task = Util.createTask {
            player.inventory.addItem(gun.createItemStack())
        }
        Bukkit.getScheduler().runTaskLater(ZombieHero.plugin, task, 1)
    }
}