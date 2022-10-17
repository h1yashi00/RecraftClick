package click.recraft.lobby

import click.recraft.share.KotlinPlugin
import click.recraft.share.RedisManager
import click.recraft.share.database.PlayerManager
import click.recraft.share.extension.TaskGenerator
import org.bukkit.Bukkit
import org.bukkit.event.Listener
import redis.clients.jedis.JedisPool

class Main: KotlinPlugin() {
    private val listeners: List<Listener> by lazy {
        listOf(
            PlayerJoin(),
            Protect(),
            MenuServerSelect,
            PlayerQuit()
        )
    }
    companion object {
        lateinit var plugin: KotlinPlugin
    }
    override fun onEnable() {
        TaskGenerator.plugin = this
        this.server.messenger.registerOutgoingPluginChannel(this, "BungeeCord")
        Bukkit.getWorld("world")!!.isAutoSave = false
        plugin = this
        RedisManager.load(JedisPool("redis", 6379))
        MenuServerSelect.load()
        MenuPlayerStats.load()
        MenuPlayerZombieHeroStats.load()
        PlayerManager.initialize("db", "recraft")
        super.onEnable()
        listeners.forEach {server.pluginManager.registerEvents(it, this)}
    }

    override fun onDisable() {
    }
}