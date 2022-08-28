package click.recraft.zombiehero

import click.recraft.share.*
import click.recraft.zombiehero.gun.GrenadeListener
import click.recraft.zombiehero.gun.api.ReloadManager
import click.recraft.zombiehero.item.PlayerGunListener
import click.recraft.zombiehero.item.PlayerGunManager
import click.recraft.zombiehero.item.gun.ShootManager
import click.recraft.zombiehero.melee.MeleeCoolDownManager
import click.recraft.zombiehero.melee.MeleeManager
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
    val reloadManager    : ReloadManager    by lazy { ReloadManager()    }
    val grenadeManager   : GrenadeManager   by lazy { GrenadeManager()   }
    val meleeManager     : MeleeManager     by lazy { MeleeManager()     }
    val meleeCoolDownManager: MeleeCoolDownManager by lazy { MeleeCoolDownManager() }
    val touchGrenadeManager: TouchGrenadeManager by lazy { TouchGrenadeManager() }
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
        Bukkit.getScheduler().runTaskTimer(this, fiveTickTask, 10, 5)
        Bukkit.getScheduler().runTaskTimer(this, oneTickTask, 10, 1)
        server.pluginManager.registerEvents(PlayerJoin(), this)
        server.pluginManager.registerEvents(PlayerQuit(), this)
        server.pluginManager.registerEvents(this, this)
        server.pluginManager.registerEvents(PlayerDamage(), this)
        server.pluginManager.registerEvents(MonsterSpawn(), this)
        server.pluginManager.registerEvents(GrenadeListener(grenadeManager), this)
        server.pluginManager.registerEvents(PlayerGunListener(playerGunManager), this)
        server.pluginManager.registerEvents(TouchGrenadeListener(touchGrenadeManager), this)
        server.pluginManager.registerEvents(MeleeListener(meleeManager), this)
        super.onEnable()
    }

    override fun onDisable() {
    }
}