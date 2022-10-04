package click.recraft.share

import click.recraft.share.protocol.ChannelMessage
import click.recraft.share.protocol.MessageType
import click.recraft.share.protocol.ServerInfo
import redis.clients.jedis.Jedis
import redis.clients.jedis.JedisPool

object RedisManager {
    private lateinit var pool: JedisPool
    private var debug = false
    fun getResource(): Jedis {
        return pool.resource!!
    }
    fun debugging() {
        debug = true
    }
    fun load(pool: JedisPool) {
        this.pool = pool
    }

    fun removeValueTransaction(key: String, filed: String) {
        val jedis = getResource()
        jedis.watch(key)
        val multi = jedis.multi()
        multi.hdel(key, filed)
        multi.exec()
        jedis.unwatch()
        jedis.close()
    }
    private fun transaction(key: String, filed: String, value: String) {
        val jedis = getResource()
        jedis.watch(key)
        val multi = jedis.multi()
        multi.hset(key, filed, value)
        multi.exec()
        jedis.unwatch()
        jedis.close()
    }

    fun serverUpdatePhase(): ServerInfo? {
        if (debug) return ServerInfo("debug", 0,0,0,0)
        val containerID = System.getenv("SERVER_NAME")
        val info = getServers()[containerID] ?: return null
        if (info.maxPhase <= info.currentPhase) {
            publishToBungee(ChannelMessage(MessageType.DELETE))
            return null
        }
        info.currentPhase += 1
        transaction("servers", containerID, info.toJson())
        return info
    }
    fun serverIsReady(): ServerInfo? {
        val containerID = System.getenv("SERVER_NAME")
        val info = getServers()[containerID] ?: return null
        info.currentPhase = 1
        transaction("servers", containerID, info.toJson())
        return info
    }
    fun registerServer(info: ServerInfo) {
        transaction("servers",info.containerId, info.toJson())
    }
    fun publishToBungee(msg: ChannelMessage) {
        val jedis = getResource()
        jedis.publish("bungee", msg.toJson())
        jedis.close()
    }

    fun getServers(): HashMap<String, ServerInfo> {
        val jedis = getResource()
        val servers = hashMapOf<String, ServerInfo>()
        jedis.hgetAll("servers").forEach { (key, info) ->
            servers[key] = ServerInfo.fromJson(info)
        }
        jedis.close()
        return servers
    }
}