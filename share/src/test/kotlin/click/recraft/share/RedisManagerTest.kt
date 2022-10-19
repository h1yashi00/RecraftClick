package click.recraft.share

import click.recraft.share.protocol.ChannelMessage
import click.recraft.share.protocol.MessageType
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import redis.clients.jedis.JedisPool

internal class RedisManagerTest {
    @BeforeEach
    fun before()  {
    }
    @Test
    fun i() {
        RedisManager.load(JedisPool("server", 6379))
        val a= RedisManager.getServers()
        println(a.size)
    }
    @Test
    fun a() {
        RedisManager.load(JedisPool("server", 6379))
        val msg = ChannelMessage(
            MessageType.CREATE,
            "Debug"
        )
        RedisManager.publishToBungee(msg)
    }
}