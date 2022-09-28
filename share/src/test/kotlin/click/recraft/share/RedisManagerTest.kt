package click.recraft.share

import click.recraft.share.protocol.ServerInfo
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import redis.clients.jedis.Jedis

internal class RedisManagerTest {
    @BeforeEach
    fun before()  {
        RedisManager.load(Jedis("localhost", 6379))
    }
    @Test
    fun i() {
        val a= RedisManager.getServers()
        println("a = ==[$a")
    }
    @Test
    fun a() {
        val info = ServerInfo(
            "test",
            0,
            0,
            10,
            10
        )
        RedisManager.setServer(info)
        println(RedisManager.getServers())
    }
}