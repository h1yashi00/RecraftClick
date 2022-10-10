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
) {
}

object Database {
    private lateinit var con: Connection
    private var plugin: JavaPlugin? = null
    private val savePlayerData: HashMap<UUID, PlayerData> = hashMapOf()
    private val savePlayerOptoin: HashMap<UUID, PlayerOption> = hashMapOf()

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
    fun removePlayerData(player: Player) {
        savePlayerOptoin.remove(player.uniqueId)
        savePlayerData.remove(player.uniqueId)
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
}