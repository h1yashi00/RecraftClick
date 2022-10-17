package click.recraft.share.database

import click.recraft.share.database.table.TableItem
import click.recraft.share.database.table.TableUser
import click.recraft.share.database.table.TableUserItem
import click.recraft.share.database.table.TableUserOption
import click.recraft.share.extension.runTaskAsync
import click.recraft.share.extension.runTaskSync
import org.bukkit.entity.Player
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*
import kotlin.collections.HashMap

object PlayerManager {
    enum class WeaponType {
        MELEE,
        GUN
    }
    private val users: HashMap<UUID, UserEntity> = hashMapOf()
    fun initialize(ip: String, database: String) {
        Database.connect("jdbc:mysql://$ip/$database", driver = "com.mysql.jdbc.Driver", user = "root", password = "narikakeisgod")
        transaction {
            SchemaUtils.create(TableUser, TableUserOption, TableUserItem, TableItem)
            val daoItem = click.recraft.share.database.dao.Item
            Item.values().forEach { item ->
                val dbItem = daoItem.findById(item.id)
                if (dbItem != null) return@forEach
                daoItem.new(item.id) {
                    name = item.name
                    price = item.price
                }
            }
        }
    }

    fun get(player: Player): UserEntity {
        if (!users.contains(player.uniqueId)) {
            player.kickPlayer("データベースから情報を得られませんでした｡")
        }
        return users[player.uniqueId]!!
    }

    fun login(player: Player) {
        runTaskAsync {
            transaction {
                users[player.uniqueId] = UserEntity(player.uniqueId, player.name).apply {
                    user.online = true
                    runTaskSync {
                        if (option.autoLoadResourcePack) {
                            player.setResourcePack("https://www.dropbox.com/s/u5o5pydskkjohc3/Archive.zip?dl=1")
                        }
                    }
                }
            }
        }
    }

    fun changeAutoLoadResourcePack(player: Player) {
        val entity = get(player)
        runTaskAsync {
            transaction {
                val boolean = !entity.option.autoLoadResourcePack
                entity.option.autoLoadResourcePack = boolean
            }
        }
    }

    fun logout(player: Player) {
        val entity = get(player)
        runTaskAsync {
            transaction {
                entity.user.online = false
            }
        }
        users.remove(player.uniqueId)
    }

    fun unlock(player: Player, item: Item) {
        val entity = get(player)
        val userItem = entity.item
        runTaskAsync {
            transaction {
                when(item) {
                    Item.MAIN_AK47 ->
                        userItem.mainAk47 = true
                    Item.MAIN_AWP ->
                        userItem.mainAwp= true
                    Item.MAIN_SAIGA ->
                        userItem.mainSaiga = true
                    Item.MAIN_M870 ->
                        userItem.mainM870 = true
                    Item.MAIN_MP5 ->
                        userItem.mainMp5 = true
                    Item.MAIN_MOSIN ->
                        userItem.mainMosin = true
                    Item.SUB_DESERT_EAGLE ->
                        userItem.subDesert_eagle= true
                    Item.SUB_GLOCK ->
                        userItem.subGlock = true
                    Item.MELEE_NATA ->
                        userItem.meleeNata = true
                    Item.MELEE_HAMMER ->
                        userItem.meleeHammer = true
                    Item.SKILL_AMMO_DUMP ->
                        userItem.skillAmmo_dump = true
                    Item.SKILL_GRENADE ->
                        userItem.skillGrenade = true
                    Item.SKILL_ZOMBIE_GRENADE ->
                        userItem.skillZombie_grenade = true
                    Item.SKILL_ZOMBIE_GRENADE_TOUCH ->
                        userItem.skillZombie_grenade_touch = true
                }
            }
        }
    }
    fun changeMain(player: Player, item: Item): Boolean {
        val daoItem = click.recraft.share.database.dao.Item
        val entity = get(player)
        if (!entity.isUnlocked(item)) {
            return false
        }
        runTaskAsync {
            transaction {
                val dbItem = daoItem.findById(item.id) ?: daoItem.new (item.id) { name = item.name ; price = item.price }
                entity.option.itemMain = dbItem.id
            }
        }
        return true
    }
    fun changeSub(player: Player, item: Item): Boolean {
        val daoItem = click.recraft.share.database.dao.Item
        val entity = get(player)
        if (!entity.isUnlocked(item)) {
            return false
        }
        runTaskAsync {
            transaction {
                val dbItem = daoItem.findById(item.id) ?: daoItem.new (item.id) { name = item.name ; price = item.price }
                entity.option.itemSub = dbItem.id
            }
        }
        return true
    }
    fun changeMelee(player: Player, item: Item): Boolean {
        val daoItem = click.recraft.share.database.dao.Item
        val entity = get(player)
        if (!entity.isUnlocked(item)) {
            return false
        }
        runTaskAsync {
            transaction {
                val dbItem = daoItem.findById(item.id) ?: daoItem.new (item.id) { name = item.name ; price = item.price }
                entity.option.itemMelee = dbItem.id
            }
        }
        return true
    }
    fun changeSkill(player: Player, item: Item): Boolean {
        val daoItem = click.recraft.share.database.dao.Item
        val entity = get(player)
        if (!entity.isUnlocked(item)) {
            return false
        }
        runTaskAsync {
            transaction {
                val dbItem = daoItem.findById(item.id) ?: daoItem.new (item.id) { name = item.name ; price = item.price }
                entity.option.itemSkill = dbItem.id
            }
        }
        return true
    }

    fun killZombie(player: Player, type: WeaponType) {
        val entity = get(player)
        runTaskAsync {
            transaction {
                entity.user.monsterKills += 1
                when (type) {
                    WeaponType.MELEE -> entity.user.meleeKills += 1
                    WeaponType.GUN -> entity.user.gunKills     += 1
                }
            }
        }
    }
    fun killHuman(player: Player) {
        val entity = get(player)
        runTaskAsync {
            transaction {
                entity.user.humanKills += 1
            }
        }
    }
    fun playGame(player: Player) {
        val entity = get(player)
        runTaskAsync {
            transaction {
                entity.user.timesPlayed += 1
            }
        }
    }
}