package click.recraft.bungee

import click.recraft.share.RedisManager
import net.md_5.bungee.api.plugin.Plugin
import redis.clients.jedis.JedisPool


class Main: Plugin() {
    private val pool = JedisPool("redis", 6379)
    override fun onEnable() {
        RedisManager.load(pool.resource!!)
        proxy.scheduler.runAsync(this) {
            pool.resource.subscribe(BungeeChannelPubSub(proxy), System.getenv("SERVER_NAME"))
        }
    }

    override fun onDisable() {
        RedisManager.shutdown()
        pool.destroy()
    }
}