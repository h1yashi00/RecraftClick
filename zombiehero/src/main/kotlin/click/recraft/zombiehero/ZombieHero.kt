package click.recraft.zombiehero

import click.recraft.share.*
import click.recraft.share.database.PlayerManager
import click.recraft.share.extension.TaskGenerator
import click.recraft.share.extension.runTaskTimer
import click.recraft.share.protocol.ServerInfo
import click.recraft.zombiehero.command.GunCommand
import click.recraft.zombiehero.command.MonsterCommand
import click.recraft.zombiehero.gun.api.ReloadManagerFullBullet
import click.recraft.zombiehero.gun.api.ReloadManagerOneBullet
import click.recraft.zombiehero.item.CustomItemFactory
import click.recraft.zombiehero.gun.api.GunListener
import click.recraft.zombiehero.gun.api.ShootManager
import click.recraft.zombiehero.item.skill.grenade.GrenadeListener
import click.recraft.zombiehero.item.skill.grenade.HitGrenadeListener
import click.recraft.zombiehero.monster.SkeletonListener
import click.recraft.zombiehero.monster.ZombieListener
import click.recraft.zombiehero.player.*
import redis.clients.jedis.JedisPool

class ZombieHero: KotlinPlugin() {
    companion object {
        lateinit var plugin: ZombieHero
    }
    val shootManager     : ShootManager by lazy { ShootManager()     }
    val reloadManagerFullBullet    : ReloadManagerFullBullet    by lazy { ReloadManagerFullBullet()    }
    val oneBulletReloadManager: ReloadManagerOneBullet by lazy { ReloadManagerOneBullet() }
    val customItemFactory: CustomItemFactory by lazy {CustomItemFactory()}
    val gameManager: GameManager by lazy { GameManager() }
    // ゲーム終了時に､すべてのタスクを終了するようにしているが､このリストに入っているtaskIdは､
    // 終了しないようにした｡
    val importantTaskId = arrayListOf<Int>()
    private var time: Int = 0
    fun getTime(): Int {
        return time
    }
    var info: ServerInfo = ServerInfo("",0,0,0, 0)
    override fun onEnable() {
        plugin = this
        TaskGenerator.plugin = this
        PlayerManager.initialize("db", "recraft")
        server.messenger.registerOutgoingPluginChannel(this, "BungeeCord")
        val containerID = System.getenv("SERVER_NAME")
        info = if (containerID == "debug") {
            RedisManager.debugging()
            ServerInfo("debug", 0,0,0,0)
        } else {
            RedisManager.load(JedisPool("redis", 6379))
            RedisManager.serverIsReady() ?: throw IllegalStateException("Cannot find $containerID server info !!!!!!!")
        }
        GameMenu.load()
        runTaskTimer(10,5) {
            importantTaskId.add(taskId)
            // playerのaction barに対して､playerのHealthを送る
            HealthManager.loopEveryOneTick()
        }
        runTaskTimer(10, 1) {
            importantTaskId.add(taskId)
            time += 1
            WalkSpeedManager.loopEveryOneTick()
            MapObjectManager.loopEveryOneTick()
            MeleeManager.loopEveryOneTick()
        }
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
        server.pluginManager.registerEvents(BulletHitListener(), this)
        server.pluginManager.registerEvents(PlayerListener(), this)
        getCommand("gun")!!.setExecutor(GunCommand())
        getCommand("monster")!!.setExecutor(MonsterCommand())
        super.onEnable()
    }

    override fun onDisable() {
        MapObjectManager.clear()
    }
}