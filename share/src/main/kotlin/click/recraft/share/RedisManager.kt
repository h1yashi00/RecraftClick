package click.recraft.share

import click.recraft.share.protocol.ServerInfo
import redis.clients.jedis.Jedis

object RedisManager {
    private lateinit var jedis: Jedis
    fun load(jedis: Jedis) {
        jedis.apply { select(0) }
        this.jedis = jedis
    }

    fun getServers(): MutableSet<ServerInfo> {
        val servers = mutableSetOf<ServerInfo>()
        jedis.hgetAll("servers").values.forEach { strInfo->
            println(strInfo)
            servers.add(ServerInfo.fromJson(strInfo))
        }
        return servers
    }

    fun setServer(info: ServerInfo) {
        println("getserver")
        jedis.watch("servers")
        val tran = jedis.multi()
        tran.hset("servers", info.containerId, info.toJson())
        tran.exec()
        println("exec")
        jedis.unwatch()
        println("unwatch")
    }
    fun shutdown() {
        jedis.shutdown()
    }
}