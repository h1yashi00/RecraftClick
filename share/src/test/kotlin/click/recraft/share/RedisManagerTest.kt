package click.recraft.share

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import redis.clients.jedis.Jedis

internal class RedisManagerTest {
    @BeforeEach
    fun before()  {
    }
    @Test
    fun i() {
        val a= RedisManager.getServers()
    }
    @Test
    fun a() {
    }
}