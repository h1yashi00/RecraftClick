package click.recraft.share.protocol

import click.recraft.share.Util
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import java.sql.Connection
import java.sql.DriverManager
import java.sql.Timestamp
import java.time.Instant
import java.util.*

enum class Rank(val rank_id: Int) {
    NONE(1),
    OWNER(2),
    MAPPER(3),
    DEVELOPER(4),
    MANAGER(5),
    YOUTUBER(6);
    companion object {
        fun getById(id: Int): Rank {
            Rank.values().forEach {
                if (it.rank_id == id) {
                    return it
                }
            }
            return NONE
        }
    }
}

data class PlayerData(
    val uuid: UUID,
    val rank: Rank,
    var currentName: String,
    val firstJoin: Timestamp,
    var lastJoin: Timestamp,
    var lastLogout: Timestamp,
)
data class PlayerZombieHeroStats (
    val uuid: UUID,
    var coin: Int = 0,
    var timesPlayed: Int = 0,
    var monsterKills: Int = 0,
    var humanKills: Int = 0,
    var gunKills: Int = 0,
    var meleeKills: Int = 0
)

enum class ItemType {
    AK47,
    MP5,
    M870,
    SAIGA,
    MOSIN,
    AWP,
    GLOCK,
    DESERT_EAGLE,
    NATA,
    HAMMER,
    AMMO_DUMP,
    GRENADE,
    ZOMBIE_GRENADE,
    ZOMBIE_HIT_GRENADE;

    override fun toString(): String {
        return name.lowercase()
    }
}


object Database {
    private lateinit var con: Connection
    private var plugin: JavaPlugin? = null
    private val savePlayerData: HashMap<UUID, PlayerData> = hashMapOf()
    private val savePlayerOptoin: HashMap<UUID, PlayerOption> = hashMapOf()
    private val savePlayerZombieHeroStats: HashMap<UUID, PlayerZombieHeroStats> = hashMapOf()
    private val savePlayerZombieHeroItem: HashMap<UUID, MutableSet<ItemType>> = hashMapOf()

    fun initialize(plugin: JavaPlugin?, url: String = "jdbc:mysql://db/recraft") {
        Class.forName("com.mysql.jdbc.Driver")
        con = DriverManager.getConnection(url, "root", "narikakeisgod")
        this.plugin = plugin
    }
    fun close() {
        con.close()
    }

    private fun createPlayerData(player: Player): PlayerData {
        val uuid = player.uniqueId
        val rank = Rank.NONE
        val currentName = player.name
        val time = Timestamp(Instant.now().toEpochMilli())
        val stat = con.prepareStatement("insert into player_data (player_uuid, current_name, first_join, last_join, last_logout, rank_id) values (?,?,?,?,?,?)")
        stat.setString(1, uuid.toString())
        stat.setString(2, currentName)
        stat.setTimestamp(3, time)
        stat.setTimestamp(4, time)
        stat.setTimestamp(5, time)
        stat.setInt(6, rank.rank_id)
        stat.executeUpdate()
        stat.close()
        return PlayerData(
            uuid,
            rank,
            currentName,
            time,
            time,
            time,
        )
    }

    private fun getPlayerData(player: Player): PlayerData {
        val sql = "select * from player_data where player_uuid = ?"
        val stat = con.prepareStatement(sql)
        stat.setString(1, player.uniqueId.toString())
        val result = stat.executeQuery()
        if (!result.next()) {
            return createPlayerData(player)
        }
        val uuid = UUID.fromString(result.getString("player_uuid"))
        val rank = Rank.getById(result.getInt("rank_id"))
        val currentName = result.getString("current_name")
        val firstJoin = result.getTimestamp("first_join")
        val lastJoin  = result.getTimestamp("last_join")
        val lastLogout = result.getTimestamp("last_logout")
        stat.close()
        return PlayerData (
            uuid,
            rank,
            currentName,
            firstJoin,
            lastJoin,
            lastLogout
        )
    }

    private fun updatePlayerData(player: Player, playerData: PlayerData) {
        val stat = con.prepareStatement("update player_data set last_join = ?, last_logout = ?, current_name = ? where player_uuid = ?")
        stat.setTimestamp(1, playerData.lastJoin)
        stat.setTimestamp(2, playerData.lastLogout)
        stat.setString(3, playerData.currentName)
        stat.setString(4, player.uniqueId.toString())
        stat.executeUpdate()
        savePlayerData[player.uniqueId] = playerData
    }

    private fun updatePlayerDataAsync(player: Player, playerData: PlayerData) {
        if (plugin == null) {
            updatePlayerData(player, playerData)
            return
        }
        val task = Util.createTask {
            updatePlayerData(player, playerData)
        }
        Bukkit.getScheduler().runTaskAsynchronously(plugin!!, task)
    }

    fun joinUpdate(player: Player) {
        getPlayerDataSync(player) {
            this.lastJoin = Timestamp.from(Instant.now())
            this.currentName = player.name
            updatePlayerDataAsync(player, this)
        }
    }
    fun quitUpdate(player: Player) {
        getPlayerDataSync(player) {
            lastLogout = Timestamp.from(Instant.now())
            updatePlayerDataAsync(player, this)
        }
    }
    // sync function
    fun getPlayerDataSync (player: Player, function: PlayerData.() -> Unit) {
        if (savePlayerData.contains(player.uniqueId)) {
            function.invoke(savePlayerData[player.uniqueId]!!)
            return
        }
        // test
        if (plugin == null) {
            val data = getPlayerData(player)
            return function.invoke(data)
        }
        val task = Util.createTask {
            val data = getPlayerData(player)
            val sync = Util.createTask {
                function.invoke(data)
            }
            Bukkit.getScheduler().runTask(plugin!!, sync)
        }
        Bukkit.getScheduler().runTaskAsynchronously(plugin!!, task)
    }

    data class PlayerOption(
        var autoResourcePackDownload: Boolean = true
    )

    private fun updatePlayerOption(player: Player, playerOption: PlayerOption) {
        val stat = con.prepareStatement("update player_option set auto_load_resource_pack = ? where player_uuid = ?")
        stat.setBoolean(1, playerOption.autoResourcePackDownload)
        stat.setString(2,  player.uniqueId.toString())
        stat.executeUpdate()
        savePlayerOptoin[player.uniqueId] = playerOption
    }

    private fun insertPlayerOption(player: Player) {
        val stat = con.prepareStatement("insert into player_option (player_uuid, auto_load_resource_pack) values (?, ?)")
        val playerOption = PlayerOption()
        stat.setString(1, player.uniqueId.toString())
        stat.setBoolean(2, playerOption.autoResourcePackDownload)
        stat.executeUpdate()
        savePlayerOptoin[player.uniqueId] = playerOption
    }

    private fun getOption(player: Player): PlayerOption {
        val stat = con.prepareStatement("select * from player_option where player_uuid = ?")
        stat.setString(1, player.uniqueId.toString())
        val result = stat.executeQuery()
        if (!result.next()) {
            insertPlayerOption(player)
        }
        else {
            savePlayerOptoin[player.uniqueId] = PlayerOption(
                result.getBoolean("auto_load_resource_pack")
            )
        }
        return savePlayerOptoin[player.uniqueId]!!
    }
    fun changeAutoLoadResourcePack(player: Player) {
        val data = getOption(player)
        data.autoResourcePackDownload = !data.autoResourcePackDownload
        updatePlayerOption(player, data)
    }

    fun getPlayerOption(player: Player, function: PlayerOption.() -> Unit) {
        if (savePlayerOptoin.contains(player.uniqueId)) {
            function.invoke(savePlayerOptoin[player.uniqueId]!!)
            return
        }
        if (plugin == null) {
            function.invoke(getOption(player))
        }
        else {
            val async = Util.createTask {
                val dataOption = getOption(player)
                val sync = Util.createTask {
                    function.invoke(dataOption)
                }
                Bukkit.getScheduler().runTask(plugin!!, sync)
            }
            Bukkit.getScheduler().runTaskAsynchronously(plugin!!, async)
        }
    }
    //
    //
    // Zombie hero stats
    //
    //
    private fun insertPlayerZombieHeroStats(player: Player) {
        val stat = con.prepareStatement("insert into player_zombiehero_stats (player_uuid) values (?)")
        stat.setString(1, player.uniqueId.toString())
        stat.executeUpdate()
        savePlayerZombieHeroStats[player.uniqueId] = PlayerZombieHeroStats(player.uniqueId)
        stat.close()
    }
    private fun updatePlayerZombieHeroStats(stats: PlayerZombieHeroStats) {
        val stat = con.prepareStatement("update player_zombiehero_stats set coin = ?, times_played = ?, monster_kills = ?, human_kills = ?, gun_kills = ?, melee_kills = ? where player_uuid = ?")
        stat.setInt(1,stats.coin)
        stat.setInt(2,stats.timesPlayed)
        stat.setInt(3,stats.monsterKills)
        stat.setInt(4,stats.humanKills)
        stat.setInt(5,stats.gunKills)
        stat.setInt(6,stats.meleeKills)
        stat.setString(7, stats.uuid.toString())
        stat.executeUpdate()
        savePlayerZombieHeroStats[stats.uuid] = stats
        stat.close()
    }

    private fun getPlayerZombieHeroStats(player: Player): PlayerZombieHeroStats {
        val stat = con.prepareStatement("select * from player_zombiehero_stats where player_uuid = ?")
        stat.setString(1, player.uniqueId.toString())
        val result = stat.executeQuery()
        if (!result.next()) {
            insertPlayerZombieHeroStats(player)
        }
        else {
            savePlayerZombieHeroStats[player.uniqueId] = PlayerZombieHeroStats(
                player.uniqueId,
                coin = result.getInt("coin"),
                timesPlayed = result.getInt("times_played"),
                monsterKills = result.getInt("monster_kills"),
                humanKills = result.getInt("human_kills"),
                gunKills = result.getInt("gun_kills"),
                meleeKills = result.getInt("melee_kills")
            )
        }
        stat.close()
        return savePlayerZombieHeroStats[player.uniqueId]!!
    }

    fun getPlayerZombieHeroStats(player: Player, function: PlayerZombieHeroStats.() -> Unit) {
        if (savePlayerZombieHeroStats.containsKey(player.uniqueId)) {
            return function.invoke(savePlayerZombieHeroStats[player.uniqueId]!!)
        }
        if (plugin == null) {
            function.invoke(getPlayerZombieHeroStats(player))
            return
        }
        else {
            val async = Util.createTask {
                val stats = getPlayerZombieHeroStats(player)
                val sync = Util.createTask {
                    function.invoke(stats)
                }
                Bukkit.getScheduler().runTask(plugin!!, sync)
            }
            Bukkit.getScheduler().runTaskAsynchronously(plugin!!, async)
        }
    }
    private fun insertPlayerZombieHeroItem(player: Player) {
        val sql = "insert into player_zombiehero_item set player_uuid = ?"
        val stat = con.prepareStatement(sql)
        stat.setString(1, player.uniqueId.toString())
        stat.executeUpdate()
        savePlayerZombieHeroItem[player.uniqueId] = mutableSetOf(ItemType.AK47, ItemType.DESERT_EAGLE, ItemType.AMMO_DUMP)
    }
    private fun updatePlayerZombieHeroItem(player: Player, items: Set<ItemType>) {
        val sql = "update player_zombiehero_item set mp5 = ?, mosin = ?, awp = ?, m870 = ?, saiga = ?, glock = ?, nata = ?, hammer = ?, ammo_dump = ?, grenade =?, zombie_grenade = ?, zombie_hit_grenade = ? where player_uuid = ?"
        val stat = con.prepareStatement(sql)
        stat.setBoolean(1, items.contains(ItemType.MP5))
        stat.setBoolean(2, items.contains(ItemType.MOSIN))
        stat.setBoolean(3, items.contains(ItemType.AWP))
        stat.setBoolean(4, items.contains(ItemType.M870))
        stat.setBoolean(5, items.contains(ItemType.SAIGA))
        stat.setBoolean(6, items.contains(ItemType.GLOCK))
        stat.setBoolean(7, items.contains(ItemType.NATA))
        stat.setBoolean(8, items.contains(ItemType.HAMMER))
        stat.setBoolean(9, items.contains(ItemType.AMMO_DUMP))
        stat.setBoolean(10,items.contains(ItemType.GRENADE))
        stat.setBoolean(11,items.contains(ItemType.ZOMBIE_GRENADE))
        stat.setBoolean(12,items.contains(ItemType.ZOMBIE_HIT_GRENADE))
        stat.setString(13, player.uniqueId.toString())
        stat.executeUpdate()
        savePlayerZombieHeroItem[player.uniqueId] = items.toMutableSet()
    }

    private fun getPlayerZombieHeroItem(player: Player): MutableSet<ItemType> {
        val sql = "select * from player_zombiehero_item where player_uuid = ?"
        val stat = con.prepareStatement(sql)
        stat.setString(1, player.uniqueId.toString())
        val result = stat.executeQuery()
        if (!result.next()) {
            insertPlayerZombieHeroItem(player)
        }
        else {
            val items = mutableSetOf<ItemType>()
            ItemType.values().forEach {
                if (result.getBoolean(it.toString())) {
                    items.add(it)
                }
            }
            savePlayerZombieHeroItem[player.uniqueId] = items
        }
        return savePlayerZombieHeroItem[player.uniqueId]!!
    }
    fun unlockItem(player: Player, type: ItemType): Boolean {
        val items = getPlayerZombieHeroItem(player)
        if (items.contains(type)) {
            return false
        }
        items.add(type)
        updatePlayerZombieHeroItem(player, items)
        return true
    }
    fun getPlayerZombieHeroItem(player: Player, function: MutableSet<ItemType>.() -> Unit ) {
        if (savePlayerZombieHeroItem.contains(player.uniqueId)) {
            return function.invoke(savePlayerZombieHeroItem[player.uniqueId]!!)
        }
        if (plugin == null) {
            return function.invoke(getPlayerZombieHeroItem(player))
        }
        val async = Util.createTask {
            val item = getPlayerZombieHeroItem(player)
            val sync = Util.createTask {
                function.invoke(item)
            }
            Bukkit.getScheduler().runTask(plugin!!, sync)
        }
        Bukkit.getScheduler().runTaskAsynchronously(plugin!!, async)
    }
    fun killZombie(killer: Player, weaponType: WeaponType) {
        val killerStats = getPlayerZombieHeroStats(killer)
        killerStats.monsterKills += 1
        killerStats.coin += 10
        when (weaponType) {
            WeaponType.GUN -> killerStats.gunKills += 1
            WeaponType.MELEE -> killerStats.meleeKills += 1
        }
        updatePlayerZombieHeroStats(killerStats)
    }
    fun zombieKillHuman(player: Player) {
        val stats = getPlayerZombieHeroStats(player)
        stats.humanKills += 1
        stats.coin += 20
        updatePlayerZombieHeroStats(stats)
    }
    fun playGame(player: Player) {
        val stats = getPlayerZombieHeroStats(player)
        stats.timesPlayed += 1
        updatePlayerZombieHeroStats(stats)
    }
    fun removeCache(player: Player) {
        savePlayerOptoin.remove(player.uniqueId)
        savePlayerData.remove(player.uniqueId)
        savePlayerZombieHeroStats.remove(player.uniqueId)
        savePlayerZombieHeroItem.remove(player.uniqueId)
    }
}
enum class WeaponType {
    GUN,
    MELEE;
}