package click.recraft.share

import click.recraft.share.database.Item
import click.recraft.share.database.PlayerManager
import org.junit.jupiter.api.Test
import org.bukkit.entity.Player
import org.jetbrains.exposed.sql.transactions.transaction
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
    private val player2 = mock(Player::class.java) .apply {
        `when`(uniqueId).thenReturn(UUID.fromString("14687d5d-0399-49db-b302-3cce4f47bc86"))
        `when`(name).thenReturn("Narikake")
    }
    @BeforeEach
    fun startUp() {
        PlayerManager.initialize("localhost", "recraft")
    }
    @AfterEach
    fun fin() {
    }
    @Test
    fun user() {
        transaction {
            PlayerManager.login(player)
            PlayerManager.login(player2)

            PlayerManager.unlock(player, Item.MAIN_AK47)
            PlayerManager.playGame(player)
            PlayerManager.killHuman(player)
            PlayerManager.killZombie(player, PlayerManager.WeaponType.GUN)
            PlayerManager.killZombie(player, PlayerManager.WeaponType.MELEE)

            PlayerManager.logout(player)
            PlayerManager.logout(player2)
        }
    }
    @Test
    fun a() {
        var buf = Item.getMain()
        println(buf)
        buf = Item.getSub()
        println(buf)
        buf = Item.getMelee()
        println(buf)
        buf = Item.getSkill()
        println(buf)
    }
    @Test
    fun b() {
        PlayerManager.login(player)
        PlayerManager.changeMain(player, Item.MAIN_AWP)
        PlayerManager.changeSub (player, Item.SUB_GLOCK)
        PlayerManager.changeMelee(player, Item.MELEE_NATA)
        PlayerManager.changeSkill(player, Item.SKILL_GRENADE)
        PlayerManager.get(player).apply {
            println(Item.getSkillById(option))
            println(Item.getMainById(option))
            println(Item.getSubById(option))
            println(Item.getMeleeById(option))
        }
    }
    @Test
    fun c() {
        PlayerManager.login(player)
        PlayerManager.get(player).apply {println(user.coin)}
    }
}