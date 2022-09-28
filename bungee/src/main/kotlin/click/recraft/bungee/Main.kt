package click.recraft.bungee

import net.md_5.bungee.api.ProxyServer
import net.md_5.bungee.api.plugin.Plugin
import redis.clients.jedis.Jedis
import redis.clients.jedis.JedisPubSub

class PubSub(private val proxy: ProxyServer) : JedisPubSub() {
    override fun onSubscribe(channel: String?, subscribedChannels: Int) {
        proxy.logger.info("$channel is Subscribed!!!")
    }

    override fun onMessage(channel: String?, message: String?) {
        proxy.logger.info("$channel was on message -> $message")
    }
}

class Main: Plugin() {
    lateinit var jedis : Jedis
    override fun onEnable() {
        jedis = Jedis("redis", 6379)
        jedis.subscribe(PubSub(proxy), "C1")
    }

    override fun onDisable() {
        jedis.shutdown()
    }
}