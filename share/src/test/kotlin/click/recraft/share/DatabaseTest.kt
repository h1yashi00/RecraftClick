package click.recraft.share

import click.recraft.share.database.Item
import click.recraft.share.database.PlayerManager
import click.recraft.share.database.table.TableUserDailyQuest
import org.junit.jupiter.api.Test
import org.bukkit.entity.Player
import org.jetbrains.exposed.sql.SchemaUtils
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
    }
    @Test
    fun d() {
        PlayerManager.login(player)
        PlayerManager.changeAutoLoadResourcePack(player)
        PlayerManager.logout(player)
    }
    @Test
    fun e() {
        PlayerManager.login(player)
        PlayerManager.unlock(player, Item.MAIN_AK47)
    }
    @Test
    fun f() {
        PlayerManager.login(player)
        (1..10).forEach {
            PlayerManager.playGame(player)
        }
    }
    @Test
    fun g() {
        PlayerManager.login(player)
        PlayerManager.receivedQuest(player, PlayerManager.QuestColumn.ONE)
    }
    // questをreceiveしていなかったとき
    @Test
    fun playerDoestNotEnableReceived() {
        transaction {
            SchemaUtils.drop(TableUserDailyQuest)
            SchemaUtils.create(TableUserDailyQuest)
        }
        PlayerManager.login(player)
        val column = PlayerManager.QuestColumn.ONE
        (0..15).forEach {
            PlayerManager.killZombie(player, PlayerManager.WeaponType.GUN)
            PlayerManager.killHuman(player)
            PlayerManager.playGame(player)
        }
        println(PlayerManager.getClonedData(player).dailyQuests[column])
    }
    // questをreceiveしたとき
    @Test
    fun playerDoesEnableReceived() {
        transaction {
            SchemaUtils.drop(TableUserDailyQuest)
            SchemaUtils.create(TableUserDailyQuest)
        }
        PlayerManager.login(player)
        val column = PlayerManager.QuestColumn.ONE
        PlayerManager.receivedQuest(player, column)
        (0..15).forEach {
            PlayerManager.killZombie(player, PlayerManager.WeaponType.GUN)
            PlayerManager.killHuman(player)
            PlayerManager.playGame(player)
        }
        PlayerManager.getClonedData(player).dailyQuests[column]
        println(PlayerManager.getClonedData(player).dailyQuests[column])
    }
}