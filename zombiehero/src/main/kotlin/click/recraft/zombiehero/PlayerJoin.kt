package click.recraft.zombiehero

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
        player.walkSpeed = 0.20F
        player.inventory.clear()
        player.inventory.addItem(ShopMenu.shop.getItem())
        ZombieHero.plugin.guns.forEach {
            player.inventory.addItem(it.getItem())
        }
        val sword = ZombieHero.plugin.swords.first()
        player.inventory.addItem(sword.getItem())
        ZombieHero.plugin.grenades.forEach {
            player.inventory.addItem(it.getItem())
        }
    }
}