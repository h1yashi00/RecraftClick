package click.recraft.share

import click.recraft.share.protocol.Database
import click.recraft.share.protocol.WeaponType
import org.junit.jupiter.api.Test
import org.bukkit.entity.Player
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import java.util.*

class DatabaseTest {
    private val player = mock(Player::class.java).apply {
        `when`(uniqueId).thenReturn(UUID.fromString("372dc1e6-30b3-4201-a5e8-69f690483bae"))
        `when`(name).thenReturn("hankake80")
    }
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
        Database.getPlayerDataSync (player) {
            println(firstJoin)
            println(lastJoin)
            println(uuid)
        }
    }
    @Test
    fun join() {
        Database.joinUpdate(player)
    }
    @Test
    fun quit() {
        Database.quitUpdate(player)
    }
    @Test
    fun changeNameJoin() {
        Database.joinUpdate(player)
    }
    @Test
    fun b() {
        Database.getPlayerOption(player) {
            println(autoResourcePackDownload)
        }
    }
    @Test
    fun c() {
        Database.getPlayerZombieHeroStats(player) {
            println(coin)
        }
    }
    @Test
    fun humanKillZombieWithGun() {
        Database.killZombie(player, WeaponType.GUN)
    }
    @Test
    fun humanKillZombieWithMelee() {
        Database.killZombie(player, WeaponType.MELEE)
    }
    @Test
    fun humanWasKilledByZombie() {
        Database.zombieKillHuman(player)
    }
    @Test
    fun playerPlayCount() {
        Database.playGame(player)
    }
    @Test
    fun gainCoin() {
        Database.coin(player, 50)
    }
    @Test
    fun subtractCoin() {
        Database.coin(player, -50)
    }
}