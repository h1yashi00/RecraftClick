package click.recraft.zombiehero

import click.recraft.zombiehero.gun.api.OneShot
import click.recraft.zombiehero.gun.api.ReloadOneBullet
import click.recraft.zombiehero.gun.base.Tick
import click.recraft.zombiehero.item.PlayerGun
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
        val gun = PlayerGun(
            ZombieHero.plugin.playerGunManager,
            10,
            ZombieHero.plugin.shootManager,
            "AK-47",
            ReloadOneBullet(
                30,
                Tick.sec(1.3)
            ),
            OneShot(
                Tick.sec(0.3),
                10,
                0.3,
                0.01,
                0.0F,
                0.0F,
            ),
            0.25F
        )
        gun.initialize()
        player.inventory.addItem(gun.itemStack)
    }
}