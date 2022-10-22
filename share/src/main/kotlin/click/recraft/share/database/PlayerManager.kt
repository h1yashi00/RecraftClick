package click.recraft.share.database

import click.recraft.share.database.dao.*
import click.recraft.share.database.player.Default
import click.recraft.share.database.table.*
import click.recraft.share.extension.runTaskAsync
import click.recraft.share.extension.runTaskSync
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.HashMap

object PlayerManager {
    enum class WeaponType {
        MELEE,
        GUN
    }
    enum class QuestColumn {
        ONE,
        TWO,
        THREE;
    }

    data class DailyQuest(
        val quest: Quest,
        var isFinished: Boolean,
        var isReceived: Boolean,
        var counter: Int
    ) {
        fun clone(): DailyQuest {
            return DailyQuest(quest, isFinished, isReceived, counter)
        }
    }

    fun getDailyQuests(userDailyQuest: UserDailyQuest): HashMap<QuestColumn, DailyQuest> {
        val hashMap: HashMap<QuestColumn, DailyQuest> = hashMapOf()
        hashMap[QuestColumn.ONE] = DailyQuest(
            Quest.getById(userDailyQuest.dailyQuest1.value)!!,
            userDailyQuest.dailyQuestFinished1,
            userDailyQuest.dailyQuestReceived1,
            userDailyQuest.dailyQuestCounter1
        )
        hashMap[QuestColumn.TWO] = DailyQuest(
            Quest.getById(userDailyQuest.dailyQuest2.value)!!,
            userDailyQuest.dailyQuestFinished2,
            userDailyQuest.dailyQuestReceived2,
            userDailyQuest.dailyQuestCounter2
        )
        hashMap[QuestColumn.THREE] = DailyQuest(
            Quest.getById(userDailyQuest.dailyQuest3.value)!!,
            userDailyQuest.dailyQuestFinished3,
            userDailyQuest.dailyQuestReceived3,
            userDailyQuest.dailyQuestCounter3
        )
        return hashMap
    }

    // プレイヤーのデータを取得するだけのデータ
    data class ClonedPlayerData(
        val coin: Int                ,
        val name: String             ,
        val firstLogin: LocalDateTime,
        val lastLogin:  LocalDateTime,
        val lastLogout: LocalDateTime,
        val timesPlayed: Int         ,
        val humanKills: Int          ,
        val monsterKills: Int        ,
        val gunKills: Int            ,
        val meleeKills: Int          ,
        val online: Boolean          ,
        val autoLoadResourcePack: Boolean,
        val itemMain: Item          ,
        val itemSub: Item           ,
        val itemMelee: Item         ,
        val itemSkill: Item         ,
        val dailyQuests : Map<QuestColumn, DailyQuest>,
        val unlockedItems: Set<Item>
    )
    // データを書き換えて任意のタイミングでupdateすることでデータベースの値を書き換える
    data class PlayerData(private val dao: DaoPlayer) {
        var coin: Int               = dao.user.coin
        var name: String            = dao.user.name
        var firstLogin: LocalDateTime = dao.user.firstLogin
        var lastLogin:  LocalDateTime = dao.user.lastLogin
        var lastLogout: LocalDateTime = dao.user.lastLogout
        var timesPlayed: Int        = dao.user.timesPlayed
        var humanKills: Int         = dao.user.humanKills
        var monsterKills: Int       = dao.user.monsterKills
        var gunKills: Int           = dao.user.gunKills
        var meleeKills: Int         = dao.user.meleeKills
        var online: Boolean         = dao.user.online
        var autoLoadResourcePack: Boolean = dao.userOption.autoLoadResourcePack
        var itemMain: Item          = Item.getMainById(dao.userOption.itemMain.value)
        var itemSub: Item           = Item.getSubById(dao.userOption.itemSub.value)
        var itemMelee: Item         = Item.getMeleeById(dao.userOption.itemMelee.value)
        var itemSkill: Item         = Item.getSkillById(dao.userOption.itemSkill.value)
        val dailyQuests : HashMap<QuestColumn, DailyQuest> = getDailyQuests(dao.userDailyQuest)
        private fun clonedQuest(): Map<QuestColumn, DailyQuest> {
            return dailyQuests.map {
                (it.key
                to
                it.value.clone()
                )
            }.toMap()
        }
        fun clone(): ClonedPlayerData {
            return ClonedPlayerData(
                coin,
                name,
                firstLogin,
                lastLogin,
                lastLogout,
                timesPlayed,
                humanKills,
                monsterKills,
                gunKills,
                meleeKills,
                online,
                autoLoadResourcePack,
                itemMain,
                itemSub,
                itemMelee,
                itemSkill,
                clonedQuest(),
                unlockedItems.toSet()
            )
        }
        private fun extractItem(userItem: UserItem): MutableSet<Item> {
            val buf = mutableSetOf<Item>()
            Item.values().forEach { item ->
                val result = when(item) {
                    Item.MAIN_AK47          -> userItem.mainAk47
                    Item.MAIN_AWP           -> userItem.mainAwp
                    Item.MAIN_SAIGA         -> userItem.mainSaiga
                    Item.MAIN_M870          -> userItem.mainM870
                    Item.MAIN_MP5           -> userItem.mainMp5
                    Item.MAIN_MOSIN         -> userItem.mainMosin
                    Item.SUB_DESERT_EAGLE   -> userItem.subDesert_eagle
                    Item.SUB_GLOCK          -> userItem.subGlock
                    Item.MELEE_NATA         -> userItem.meleeNata
                    Item.MELEE_HAMMER       -> userItem.meleeHammer
                    Item.SKILL_AMMO_DUMP    -> userItem.skillAmmo_dump
                    Item.SKILL_GRENADE      -> userItem.skillGrenade
                    Item.SKILL_ZOMBIE_GRENADE -> userItem.skillZombie_grenade
                    Item.SKILL_ZOMBIE_GRENADE_TOUCH -> userItem.skillZombie_grenade_touch
                }
                if (result) buf.add(item)
            }
            return buf
        }

        private val unlockedItems = extractItem(dao.userItem)
        fun isItemUnlocked(item: Item): Boolean {
            return unlockedItems.contains(item)
        }
        fun unlockItem(item: Item) {
            unlockedItems.add(item)
        }
        // daoのクラスのフィールドに値の変更をかけると､updateQueryが流れるので､ここ以外ではdaoを利用しないようにする｡(PlayerDataを作成する際のデータ取得は例外)
        fun update() {
            if (dao.user.coin != coin) {
                dao.user.coin = coin
            }
            if (dao.user.name != name) {
                dao.user.name = name
            }
            if (dao.user.lastLogin != lastLogin) {
                dao.user.lastLogout = lastLogout
            }
            if (dao.user.timesPlayed != timesPlayed) {
                dao.user.timesPlayed = timesPlayed
            }
            if (dao.user.humanKills != humanKills) {
                dao.user.humanKills = humanKills
            }
            if (dao.user.monsterKills != monsterKills) {
                dao.user.monsterKills = monsterKills
            }
            if (dao.user.gunKills != gunKills) {
                dao.user.gunKills = gunKills
            }
            if (dao.user.meleeKills != meleeKills) {
                dao.user.meleeKills = meleeKills
            }
            if (dao.user.online != online) {
                dao.user.online = online
            }
            if (dao.userOption.autoLoadResourcePack != autoLoadResourcePack) {
                dao.userOption.autoLoadResourcePack = autoLoadResourcePack
            }
            if (dao.userOption.itemMain.value != itemMain.id) {
                dao.userOption.itemMain = click.recraft.share.database.dao.Item.findById(itemMain.id)!!.id
            }
            if (dao.userOption.itemSub.value != itemSub.id) {
                dao.userOption.itemSub = click.recraft.share.database.dao.Item.findById(itemSub.id)!!.id
            }
            if (dao.userOption.itemMelee.value != itemMelee.id) {
                dao.userOption.itemMelee = click.recraft.share.database.dao.Item.findById(itemMelee.id)!!.id
            }
            if (dao.userOption.itemSkill.value != itemSkill.id) {
                dao.userOption.itemSkill = click.recraft.share.database.dao.Item.findById(itemSkill.id)!!.id
            }

            val dailyQuest1= dailyQuests[QuestColumn.ONE]!!
            if (dailyQuest1.isReceived != dao.userDailyQuest.dailyQuestReceived1) {
                dao.userDailyQuest.dailyQuestReceived1 = dailyQuest1.isReceived
            }
            if (dailyQuest1.isFinished != dao.userDailyQuest.dailyQuestFinished1) {
                dao.userDailyQuest.dailyQuestFinished1 = dailyQuest1.isFinished
            }
            if (dailyQuest1.counter != dao.userDailyQuest.dailyQuestCounter1) {
                dao.userDailyQuest.dailyQuestCounter1 = dailyQuest1.counter
            }

            val dailyQuest2= dailyQuests[QuestColumn.TWO]!!
            if (dailyQuest2.isReceived != dao.userDailyQuest.dailyQuestReceived2) {
                dao.userDailyQuest.dailyQuestReceived2 = dailyQuest2.isReceived
            }
            if (dailyQuest2.isFinished != dao.userDailyQuest.dailyQuestFinished2) {
                dao.userDailyQuest.dailyQuestFinished2 = dailyQuest2.isFinished
            }
            if (dailyQuest2.counter != dao.userDailyQuest.dailyQuestCounter2) {
                dao.userDailyQuest.dailyQuestCounter2 = dailyQuest2.counter
            }

            val dailyQuest3= dailyQuests[QuestColumn.THREE]!!
            if (dailyQuest3.isReceived != dao.userDailyQuest.dailyQuestReceived3) {
                dao.userDailyQuest.dailyQuestReceived3 = dailyQuest3.isReceived
            }
            if (dailyQuest3.isFinished != dao.userDailyQuest.dailyQuestFinished3) {
                dao.userDailyQuest.dailyQuestFinished3 = dailyQuest3.isFinished
            }
            if (dailyQuest3.counter != dao.userDailyQuest.dailyQuestCounter3) {
                dao.userDailyQuest.dailyQuestCounter3 = dailyQuest3.counter
            }

            unlockedItems.forEach { item ->
                val userItem = dao.userItem
                when (item) {
                    Item.MAIN_AK47 ->   if (!userItem.mainAk47)   { userItem.mainAk47 = true  }
                    Item.MAIN_AWP ->    if (!userItem.mainAwp)    { userItem.mainAwp = true   }
                    Item.MAIN_SAIGA ->  if (!userItem.mainSaiga)    { userItem.mainSaiga = true }
                    Item.MAIN_M870 ->   if (!userItem.mainM870)     { userItem.mainM870 = true }
                    Item.MAIN_MP5 ->    if (!userItem.mainMp5)      {userItem.mainMp5 = true }
                    Item.MAIN_MOSIN ->  if (!userItem.mainMosin)   {userItem.mainMosin = true }
                    Item.SUB_DESERT_EAGLE -> if (!userItem.subDesert_eagle) {userItem.subDesert_eagle = true}
                    Item.SUB_GLOCK ->  if (!userItem.subGlock) {userItem.subGlock = true}
                    Item.MELEE_NATA -> if (!userItem.meleeNata) {userItem.meleeNata = true}
                    Item.MELEE_HAMMER -> if (!userItem.meleeHammer) {userItem.meleeHammer = true}
                    Item.SKILL_AMMO_DUMP -> if (!userItem.skillAmmo_dump) {userItem.skillAmmo_dump = true}
                    Item.SKILL_GRENADE ->  if (!userItem.skillGrenade)  {userItem.skillGrenade = true}
                    Item.SKILL_ZOMBIE_GRENADE -> if (!userItem.skillZombie_grenade) {userItem.skillGrenade = true}
                    Item.SKILL_ZOMBIE_GRENADE_TOUCH -> if(!userItem.skillZombie_grenade_touch) {userItem.skillZombie_grenade_touch = true}
                }
            }
        }
    }
    class DaoPlayer(
        player: Player,
    ) {
        val user = User.findById(player.uniqueId) ?: User.new(player.uniqueId) { name = player.name}
        val userItem = UserItem.findById(player.uniqueId) ?: UserItem.new(player.uniqueId){}
        private val daoItem = click.recraft.share.database.dao.Item
        val userOption = UserOption.findById(player.uniqueId) ?: UserOption.new(player.uniqueId) {
            itemMain = daoItem.findById(Default.itemMain.id)?.id ?: daoItem.new(Default.itemMain.id) {}.id
            itemSub  = daoItem.findById(Default.itemSub.id) ?.id  ?: daoItem.new(Default.itemSub.id) {}.id
            itemMelee = daoItem.findById(Default.itemMelee.id) ?.id ?: daoItem.new(Default.itemMelee.id) {}.id
            itemSkill = daoItem.findById(Default.itemSkill.id) ?. id ?: daoItem.new(Default.itemSkill.id) {}.id
        }
        val userDailyQuest = UserDailyQuest.findById(player.uniqueId) ?: UserDailyQuest.new(player.uniqueId) {}
    }

    private val playerDatas: HashMap<UUID, PlayerData> = hashMapOf()
    fun initialize(ip: String, database: String) {
        Database.connect("jdbc:mysql://$ip/$database", driver = "com.mysql.jdbc.Driver", user = "root", password = "narikakeisgod")
        transaction {
            SchemaUtils.create(TableItem, TableDailyQuest)
            val daoItem = click.recraft.share.database.dao.Item
            Item.values().forEach { item ->
                val dbItem = daoItem.findById(item.id)
                if (dbItem != null) return@forEach
                daoItem.new(item.id) {
                    name = item.name
                }
            }
            Quest.values().forEach {
                val daoDailyQuest = DailyQuest
                daoDailyQuest.findById(it.id) ?: daoDailyQuest.new(it.id) {name = it.name}
            }
            SchemaUtils.create(TableUser, TableUserOption, TableUserItem, TableUserDailyQuest)
            SchemaUtils.create(TableUserDailyQuest)
        }
    }


    private fun get(player: Player): PlayerData {
        if (!playerDatas.contains(player.uniqueId)) {
            player.kickPlayer("データベースから情報を得られませんでした｡")
        }
        return playerDatas[player.uniqueId]!!
    }
    fun getClonedData(player: Player): ClonedPlayerData {
        return get(player).clone()
    }

    fun login(player: Player) {
        runTaskAsync {
            transaction {
                val dao = DaoPlayer(player)
                val data = PlayerData(
                    dao
                )
                data.update()
                runTaskSync {
                    if (data.autoLoadResourcePack) {
                        player.setResourcePack("https://www.dropbox.com/s/u5o5pydskkjohc3/Archive.zip?dl=1")
                    }
                }
                playerDatas[player.uniqueId] = data
            }
        }
    }

    fun changeAutoLoadResourcePack(player: Player) {
        val data = get(player)
        data.autoLoadResourcePack = !data.autoLoadResourcePack
        runTaskAsync {
            transaction {
                data.update()
            }
        }
    }

    fun logout(player: Player) {
        val data = get(player)
        data.online = false
        runTaskAsync {
            transaction {
                data.update()
            }
        }
        playerDatas.remove(player.uniqueId)
    }

    fun unlock(player: Player, item: Item) {
        val data = get(player)
        if (data.isItemUnlocked(item)) {
            player.sendMessage("${ChatColor.RED}すでに開放しています｡")
            return
        }
        if (data.coin < item.price) {
            player.sendMessage("${ChatColor.RED}コインが足りません")
            return
        }
        player.sendMessage("${ChatColor.GREEN}${item}を開放しました｡")
        data.coin -= item.price
        data.unlockItem(item)
        runTaskAsync {
            transaction {
                data.update()
            }
        }
    }

    fun changeMain(player: Player, item: Item): Boolean {
        val data = get(player)
        if (!data.isItemUnlocked(item)) {
            return false
        }
        data.itemMain = item
        runTaskAsync {
            transaction {
                data.update()
            }
        }
        return true
    }
    fun changeSub(player: Player, item: Item): Boolean {
        val data = get(player)
        if (!data.isItemUnlocked(item)) {
            return false
        }
        data.itemSub = item
        runTaskAsync {
            transaction {
                data.update()
            }
        }
        return true
    }
    fun changeMelee(player: Player, item: Item): Boolean {
        val data= get(player)
        if (!data.isItemUnlocked(item)) {
            return false
        }
        data.itemMelee = item
        runTaskAsync {
            transaction {
                data.update()
            }
        }
        return true
    }
    fun changeSkill(player: Player, item: Item): Boolean {
        val data= get(player)
        if (!data.isItemUnlocked(item)) {
            return false
        }
        data.itemSkill = item
        runTaskAsync {
            transaction {
                data.update()
            }
        }
        return true
    }

    private fun receivedChangeQuestCount(data: PlayerData, quest: Quest) {
        data.dailyQuests.forEach {(_, dailyQuest) ->
            if (dailyQuest.isReceived && dailyQuest.quest == quest) {
                dailyQuest.counter += 1
            }
        }
    }

    fun killZombie(player: Player, type: WeaponType) {
        val data = get(player)
        data.monsterKills += 1
        data.coin += 5
        receivedChangeQuestCount(data, Quest.KILL_ZOMBIE)
        player.sendMessage("${ChatColor.GOLD}+5 coinを獲得した")
        when (type) {
            WeaponType.MELEE -> data.meleeKills += 1
            WeaponType.GUN ->   data.gunKills     += 1
        }
        runTaskAsync {
            transaction {
                data.update()
            }
        }
    }
    fun killHuman(player: Player) {
        val data = get(player)
        data.humanKills += 1
        data.coin += 15
        player.sendMessage("${ChatColor.GOLD}+15 coinを獲得した")
        receivedChangeQuestCount(data, Quest.KILL_HUMAN)
        runTaskAsync {
            transaction {
                data.update()
            }
        }
    }
    fun playGame(player: Player) {
        val data = get(player)
        data.timesPlayed += 1
        receivedChangeQuestCount(data,Quest.PLAY_TIMES)
        runTaskAsync {
            transaction {
                data.update()
            }
        }
    }
    fun receivedQuest(player: Player, column: QuestColumn) {
        val data = get(player)
        data.dailyQuests[column]!!.isReceived = true
        runTaskAsync {
            transaction {
                data.update()
            }
        }
    }
    fun dailyQuestFinish(player: Player, column: QuestColumn) {
        val data = get(player)
        val dailyQuest = data.dailyQuests[column]!!
        if (dailyQuest.counter >= dailyQuest.quest.finishNum) {
            data.coin += dailyQuest.quest.giveCoin
            dailyQuest.isFinished = true
            player.sendMessage("${ChatColor.GREEN}${dailyQuest.quest.displayName}を達成しました! ${ChatColor.GOLD}+${dailyQuest.quest.giveCoin} coin")
            runTaskAsync {
                transaction {
                    data.update()
                }
            }
        }
    }
}