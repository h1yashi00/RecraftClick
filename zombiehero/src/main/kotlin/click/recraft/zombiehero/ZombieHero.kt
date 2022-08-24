package click.recraft.zombiehero

import click.recraft.share.*
import click.recraft.zombiehero.grenade.GrenadeEvents
import click.recraft.zombiehero.item.PlayerGunListener
import click.recraft.zombiehero.item.PlayerGunManager
import click.recraft.zombiehero.item.gun.ShootManager
import click.recraft.zombiehero.player.HealthManager
import click.recraft.zombiehero.player.WalkSpeedManager
import org.bukkit.Bukkit
import org.bukkit.event.Listener

class ZombieHero: KotlinPlugin(), Listener {
    companion object {
        lateinit var plugin: ZombieHero
    }
    val playerGunManager : PlayerGunManager by lazy { PlayerGunManager() }
    val shootManager     : ShootManager     by lazy { ShootManager()     }
    override fun onEnable() {
        plugin = this
        ShopMenu.load()
        val fiveTickTask = Util.createTask {
            // playerのaction barに対して､playerのHealthを送る
            HealthManager.loopEveryOneTick()
        }
        val oneTickTask = Util.createTask {
            WalkSpeedManager.loopEveryOneTick()
        }
        Bukkit.getScheduler().runTaskTimer(this, oneTickTask, 10, 1)
        server.pluginManager.registerEvents(PlayerJoin(), this)
        server.pluginManager.registerEvents(PlayerQuit(), this)
        server.pluginManager.registerEvents(this, this)
        server.pluginManager.registerEvents(HoldKeyEvent(), this)
        server.pluginManager.registerEvents(PlayerDamage(), this)
        server.pluginManager.registerEvents(MonsterSpawn(), this)
//        server.pluginManager.registerEvents(SwordEvents(), this)
        server.pluginManager.registerEvents(GrenadeEvents(), this)
        server.pluginManager.registerEvents(PlayerGunListener(playerGunManager), this)
        super.onEnable()
    }

    override fun onDisable() {
    }
}