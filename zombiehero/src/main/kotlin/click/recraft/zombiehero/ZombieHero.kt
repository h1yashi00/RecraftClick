package click.recraft.zombiehero

import click.recraft.share.*
import click.recraft.zombiehero.gun.api.ReloadManagerFullBullet
import click.recraft.zombiehero.gun.api.ReloadManagerOneBullet
import click.recraft.zombiehero.item.CustomItemFactory
import click.recraft.zombiehero.item.grenade.*
import click.recraft.zombiehero.item.gun.GunListener
import click.recraft.zombiehero.item.gun.ShootManager
import click.recraft.zombiehero.item.melee.MeleeCoolDownManager
import click.recraft.zombiehero.monster.MonsterManager
import click.recraft.zombiehero.monster.SkeletonListener
import click.recraft.zombiehero.monster.ZombieListener
import click.recraft.zombiehero.player.HealthManager
import click.recraft.zombiehero.player.WalkSpeedManager
import org.bukkit.Bukkit

class ZombieHero: KotlinPlugin() {
    companion object {
        lateinit var plugin: ZombieHero
    }
    val shootManager     : ShootManager     by lazy { ShootManager()     }
    val reloadManagerFullBullet    : ReloadManagerFullBullet    by lazy { ReloadManagerFullBullet()    }
    val oneBulletReloadManager: ReloadManagerOneBullet by lazy { ReloadManagerOneBullet() }
    val meleeCoolDownManager: MeleeCoolDownManager by lazy { MeleeCoolDownManager() }
    val customItemFactory: CustomItemFactory by lazy {CustomItemFactory()}
    val monsterManager: MonsterManager      by lazy { MonsterManager() }
    private var time: Int = 0
    fun getTime(): Int {
        return time
    }
    override fun onEnable() {
        plugin = this
        ShopMenu.load()
        val fiveTickTask = Util.createTask {
            // playerのaction barに対して､playerのHealthを送る
            HealthManager.loopEveryOneTick()
        }
        val oneTickTask = Util.createTask {
            time += 1
            WalkSpeedManager.loopEveryOneTick()
        }
        Bukkit.getScheduler().runTaskTimer(this, fiveTickTask, 10, 5)
        Bukkit.getScheduler().runTaskTimer(this, oneTickTask, 10, 1)
        server.pluginManager.registerEvents(PlayerJoin(), this)
        server.pluginManager.registerEvents(PlayerQuit(), this)
        server.pluginManager.registerEvents(PlayerHealthListener(), this)
        server.pluginManager.registerEvents(MonsterSpawn(), this)
        server.pluginManager.registerEvents(GunListener(customItemFactory), this)
        server.pluginManager.registerEvents(HitGrenadeListener(customItemFactory), this)
        server.pluginManager.registerEvents(ZombieListener(monsterManager), this)
        server.pluginManager.registerEvents(ProtectWorldListener(), this)
        server.pluginManager.registerEvents(SkeletonListener(monsterManager), this)
        server.pluginManager.registerEvents(GrenadeListener(), this)
        super.onEnable()
    }

    override fun onDisable() {
    }
}