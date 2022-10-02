package click.recraft.share

import click.recraft.share.protocol.ChannelMessage
import click.recraft.share.protocol.MessageType
import click.recraft.share.protocol.ServerInfo
import redis.clients.jedis.Jedis

object RedisManager {
    private lateinit var jedis: Jedis
    fun load(jedis: Jedis) {
        jedis.apply { select(0) }
        this.jedis = jedis
    }

    fun removeValueTransaction(key: String, filed: String) {
        jedis.watch(key)
        val multi = jedis.multi()
        multi.hdel(key, filed)
        multi.exec()
        jedis.unwatch()
    }
    private fun transaction(key: String, filed: String, value: String) {
        jedis.watch(key)
        val multi = jedis.multi()
        multi.hset(key, filed, value)
        multi.exec()
        jedis.unwatch()
    }

    fun serverUpdatePhase(): ServerInfo? {
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