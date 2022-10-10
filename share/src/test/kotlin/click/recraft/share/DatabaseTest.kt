package click.recraft.share

import click.recraft.share.protocol.Database
import org.junit.jupiter.api.Test
import org.bukkit.entity.Player
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import java.util.*

class DatabaseTest {
    @BeforeEach
    fun startUp() {
        Database.initialize(null, "jdbc:mysql://localhost/recraft")
    }
    @AfterEach
    fun fin() {
        Database.close()
    }
    @Test
    fun a() {
        val player = mock(Player::class.java)
        `when`(player.uniqueId).thenReturn(UUID.fromString("372dc1e6-30b3-4201-a5e8-69f690483bae"))
        `when`(player.name).thenReturn("hankake")
        Database.getPlayerDataSync (player) {
            println(firstJoin)
            println(lastJoin)
            println(uuid)
        }
    }
    @Test
    fun join() {
        val player = mock(Player::class.java)
        `when`(player.uniqueId).thenReturn(UUID.fromString("372dc1e6-30b3-4201-a5e8-69f690483bae"))
        `when`(player.name).thenReturn("hankake")
        Database.joinUpdate(player)
    }
    @Test
    fun quit() {
        val player = mock(Player::class.java)
        `when`(player.uniqueId).thenReturn(UUID.fromString("372dc1e6-30b3-4201-a5e8-69f690483bae"))
        `when`(player.name).thenReturn("hankake")
        Database.quitUpdate(player)
    }
    @Test
    fun changeNameJoin() {
        val player = mock(Player::class.java)
        `when`(player.uniqueId).thenReturn(UUID.fromString("372dc1e6-30b3-4201-a5e8-69f690483bae"))
        `when`(player.name).thenReturn("hankake80")
        Database.joinUpdate(player)
    }
    @Test
    fun b() {
        val player = mock(Player::class.java)
        `when`(player.uniqueId).thenReturn(UUID.fromString("372dc1e6-30b3-4201-a5e8-69f690483bae"))
        `when`(player.name).thenReturn("hankake80")
        Database.getPlayerOption(player) {
            println(autoResourcePackDownload)
        }
    }
}