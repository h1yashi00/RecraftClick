package click.recraft.zombiehero

import click.recraft.share.*
import click.recraft.zombiehero.command.GunCommand
import click.recraft.zombiehero.command.MonsterCommand
import click.recraft.zombiehero.gun.api.ReloadManagerFullBullet
import click.recraft.zombiehero.gun.api.ReloadManagerOneBullet
import click.recraft.zombiehero.item.CustomItemFactory
import click.recraft.zombiehero.item.grenade.*
import click.recraft.zombiehero.gun.api.GunListener
import click.recraft.zombiehero.gun.api.ShootManager
import click.recraft.zombiehero.item.melee.MeleeCoolDownManager
import click.recraft.zombiehero.monster.SkeletonListener
import click.recraft.zombiehero.monster.ZombieListener
import click.recraft.zombiehero.player.HealthManager
import click.recraft.zombiehero.player.WalkSpeedManager
import org.bukkit.Bukkit

class ZombieHero: KotlinPlugin() {
    companion object {
        lateinit var plugin: ZombieHero
    }
    val shootManager     : ShootManager by lazy { ShootManager()     }
    val reloadManagerFullBullet    : ReloadManagerFullBullet    by lazy { ReloadManagerFullBullet()    }
    val oneBulletReloadManager: ReloadManagerOneBullet by lazy { ReloadManagerOneBullet() }
    val meleeCoolDownManager: MeleeCoolDownManager by lazy { MeleeCoolDownManager() }
    val customItemFactory: CustomItemFactory by lazy {CustomItemFactory()}
    val gameManager: GameManager by lazy { GameManager() }
    // ゲーム終了時に､すべてのタスクを終了するようにしているが､このリストに入っているtaskIdは､
    // 終了しないようにした｡
    val importantTaskId = arrayListOf<Int>()
    private var time: Int = 0
    fun getTime(): Int {
        return time
    }
    override fun onEnable() {
        plugin = this
        ShopMenu.load()
        val fiveTickTask = Util.createTask {
            importantTaskId.add(taskId)
            // playerのaction barに対して､playerのHealthを送る
            HealthManager.loopEveryOneTick()
        }
        val oneTickTask = Util.createTask {
            importantTaskId.add(taskId)
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
        server.pluginManager.registerEvents(ZombieListener(), this)
        server.pluginManager.registerEvents(ProtectWorldListener(), this)
        server.pluginManager.registerEvents(SkeletonListener(), this)
        server.pluginManager.registerEvents(GrenadeListener(), this)
        getCommand("gun")!!.setExecutor(GunCommand())
        getCommand("monster")!!.setExecutor(MonsterCommand())
        super.onEnable()
    }

    override fun onDisable() {
    }
}