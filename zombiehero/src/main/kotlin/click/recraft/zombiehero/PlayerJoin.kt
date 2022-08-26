package click.recraft.zombiehero

import click.recraft.zombiehero.gun.api.MultiShot
import click.recraft.zombiehero.gun.api.OneShot
import click.recraft.zombiehero.gun.api.ReloadFullBullet
import click.recraft.zombiehero.gun.api.ReloadOneBullet
import click.recraft.zombiehero.gun.base.Tick
import click.recraft.zombiehero.item.PlayerGun
import org.bukkit.Bukkit
import org.bukkit.Material
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
        val gun = PlayerGun (
            material = Material.STICK,
            name = "AK-47",
            customModeValue = 30,
            manager = ZombieHero.plugin.playerGunManager,
            shootManager = ZombieHero.plugin.shootManager,
            reload = ReloadOneBullet (
                7,
                Tick.sec(1.0),
                ZombieHero.plugin.reloadManager
            ),
            shot = MultiShot (
                Tick.sec(0.5),
                10,
                5,
                0.2,
                0.1,
                0.0F,
                0.0F,
                Tick.sec(0.3)
            ),
            walkSpeed = 0.25F
        )
        val gun2 = PlayerGun (
            material = Material.PINK_DYE,
            name = "WIWIWI",
            customModeValue = 30,
            manager = ZombieHero.plugin.playerGunManager,
            shootManager = ZombieHero.plugin.shootManager,
            reload = ReloadFullBullet (
                30,
                Tick.sec(1.3),
                ZombieHero.plugin.reloadManager
            ),
            shot = OneShot(
                Tick.sec(0.1),
                10,
                0.3,
                0.01,
                0.0F,
                0.0F,
                Tick.sec(0.05)
            ),
            walkSpeed = 0.25F
        )
        val gun3 = PlayerGun (
            material = Material.PINK_DYE,
            name = "WIWIWI",
            customModeValue = 30,
            manager = ZombieHero.plugin.playerGunManager,
            shootManager = ZombieHero.plugin.shootManager,
            reload = ReloadFullBullet (
                7,
                Tick.sec(1.3),
                ZombieHero.plugin.reloadManager
            ),
            shot = OneShot(
                Tick.sec(0.7),
                70,
                1.0,
                0.01,
                0.0F,
                0.0F,
                Tick.sec(0.5)
            ),
            walkSpeed = 0.25F
        )
        gun.initialize()
        gun2.initialize()
        gun3.initialize()
        val task = Util.createTask {
            gun.playerGiveItem(player)
            gun2.playerGiveItem(player)
            gun3.playerGiveItem(player)
        }
        Bukkit.getScheduler().runTaskLater(ZombieHero.plugin, task, 1)
    }
}