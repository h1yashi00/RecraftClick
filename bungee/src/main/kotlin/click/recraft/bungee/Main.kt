package click.recraft.bungee

import click.recraft.share.RedisManager
import click.recraft.share.protocol.Channel
import click.recraft.share.protocol.Request
import click.recraft.share.protocol.ServerInfo
import net.md_5.bungee.api.ProxyServer
import net.md_5.bungee.api.plugin.Plugin
import redis.clients.jedis.Jedis
import redis.clients.jedis.JedisPubSub

class PubSub(private val proxy: ProxyServer, private val jedis: Jedis) : JedisPubSub() {
    override fun onSubscribe(channel: String?, subscribedChannels: Int) {
        proxy.logger.info("$channel is Subscribed!!!")
    }
    private val save: MutableSet<ServerInfo> = mutableSetOf()

    private fun createServer() {
    }
    private fun deleteServer() {
    }
    private fun replayServersInfo(info: ServerInfo) {
        jedis.publish(info.containerId, "")
    }

    override fun onMessage(channel: String, message: String) {
        proxy.logger.info("$channel was on message -> $message")
        when (Channel.valueOf(channel)) {
            Channel.Bungee -> {
                when (Request.fromJson(message)) {
                    Request.Create -> createServer()
                    Request.Delete -> deleteServer()
                }
            }
            Channel.Server -> {
                // pass
            }
        }
    }
}

class Main: Plugin() {
    override fun onEnable() {
        RedisManager.load(Jedis("redis", 6379))
        val info = ServerInfo(
            "lobby",
            0,
            50,
            0,
            0,
        )
        println(info)
        RedisManager.setServer(info)
//        proxy.scheduler.runAsync(this) {
//            jedis.subscribe(PubSub(proxy, jedis), Channel.Bungee.toString())
//        }
    }

    override fun onDisable() {
        RedisManager.shutdown()
    }
}