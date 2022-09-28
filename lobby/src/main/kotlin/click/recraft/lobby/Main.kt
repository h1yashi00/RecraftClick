package click.recraft.lobby

import click.recraft.share.KotlinPlugin
import click.recraft.share.RedisManager
import org.bukkit.Bukkit
import redis.clients.jedis.Jedis

class Main: KotlinPlugin() {
    private val listeners = listOf(
        PlayerJoin(),
        Protect()
    )
    companion object {
        lateinit var plugin: KotlinPlugin
    }
    override fun onEnable() {
        Bukkit.getWorld("world")!!.isAutoSave = false
        plugin = this
        RedisManager.load(Jedis("redis", 6379))
        Menu.load()

        listeners.forEach {server.pluginManager.registerEvents(it, this)}
        super.onEnable()
    }

    override fun onDisable() {
        RedisManager.shutdown()
    }
}