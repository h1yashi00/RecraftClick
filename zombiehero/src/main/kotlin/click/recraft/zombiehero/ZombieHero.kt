package click.recraft.zombiehero

import click.recraft.share.*
import click.recraft.zombiehero.gun.GrenadeListener
import click.recraft.zombiehero.gun.api.ReloadManagerFullBullet
import click.recraft.zombiehero.gun.api.ReloadManagerOneBullet
import click.recraft.zombiehero.item.CustomItemFactory
import click.recraft.zombiehero.item.grenade.*
import click.recraft.zombiehero.item.gun.PlayerGunListener
import click.recraft.zombiehero.item.gun.PlayerGunScopeListener
import click.recraft.zombiehero.item.gun.ShootManager
import click.recraft.zombiehero.item.melee.MeleeCoolDownManager
import click.recraft.zombiehero.item.melee.MeleeListener
import click.recraft.zombiehero.monster.MonsterManager
import click.recraft.zombiehero.monster.SkeletonListener
import click.recraft.zombiehero.monster.ZombieListener
import click.recraft.zombiehero.player.HealthManager
import click.recraft.zombiehero.player.WalkSpeedManager
import org.bukkit.Bukkit
import org.bukkit.event.Listener

class ZombieHero: KotlinPlugin(), Listener {
    companion object {
        lateinit var plugin: ZombieHero
    }
    val shootManager     : ShootManager     by lazy { ShootManager()     }
    val reloadManagerFullBullet    : ReloadManagerFullBullet    by lazy { ReloadManagerFullBullet()    }
    val oneBulletReloadManager: ReloadManagerOneBullet by lazy { ReloadManagerOneBullet() }
    val meleeCoolDownManager: MeleeCoolDownManager by lazy { MeleeCoolDownManager() }
    val customItemFactory: CustomItemFactory by lazy {CustomItemFactory()}
    val monsterManager: MonsterManager      by lazy { MonsterManager() }
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
        server.pluginManager.registerEvents(PlayerHealthListener(), this)
        server.pluginManager.registerEvents(MonsterSpawn(), this)
        server.pluginManager.registerEvents(GrenadeListener(customItemFactory), this)
        server.pluginManager.registerEvents(PlayerGunListener(customItemFactory), this)
        server.pluginManager.registerEvents(TouchGrenadeListener(customItemFactory), this)
        server.pluginManager.registerEvents(HitGrenadeListener(customItemFactory), this)
        server.pluginManager.registerEvents(MeleeListener(customItemFactory), this)
        server.pluginManager.registerEvents(ZombieListener(monsterManager), this)
        server.pluginManager.registerEvents(ProtectWorldListener(), this)
        server.pluginManager.registerEvents(SkeletonListener(monsterManager), this)
        server.pluginManager.registerEvents(PlayerGunScopeListener(customItemFactory), this)
        super.onEnable()
    }

    override fun onDisable() {
    }
}