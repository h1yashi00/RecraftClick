package click.recraft.share

import click.recraft.share.protocol.ChannelMessage
import click.recraft.share.protocol.ServerInfo
import redis.clients.jedis.Jedis

object RedisManager {
    private lateinit var jedis: Jedis
    fun load(jedis: Jedis) {
        jedis.apply { select(0) }
        this.jedis = jedis
    }

    private fun transaction(key: String, filed: String, value: String) {
        jedis.watch(key)
        val multi = jedis.multi()
        multi.hset(key, filed, value)
        multi.exec()
        jedis.unwatch()
    }
    fun serverIsReady() {
        val containerID = System.getenv("SERVER_NAME")
        println(containerID)
        getServers().forEach { (key, info) ->
            println(key)
            if (key == containerID) {
                println(key)
                info.currentPhase = 1
                println(key)
                transaction("servers", key, info.toJson())
            }
        }
    }
    fun registerServer(info: ServerInfo) {
        transaction("servers",info.containerId, info.toJson())
    }
    fun publishToBungee(msg: ChannelMessage) {
        jedis.publish("bungee", msg.toJson())
    }

    fun getServers(): HashMap<String, ServerInfo> {
        val servers = hashMapOf<String, ServerInfo>()
        jedis.hgetAll("servers").forEach { (key, info) ->
            servers[key] = ServerInfo.fromJson(info)
        }
        return servers
    }

    fun shutdown() {
        jedis.shutdown()
    }
}