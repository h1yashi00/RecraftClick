package click.recraft.share.database.table

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime
import java.util.*

object TableUser: IdTable<UUID>("users") {
    override val id: Column<EntityID<UUID>> = uuid("uuid").entityId()
    val name       = varchar("name", 16)
    val firstLogin = datetime("first_login").default(LocalDateTime.now())
    val lastLogin  = datetime("last_login") .default(LocalDateTime.now())
    val lastLogout = datetime("last_logout").default(LocalDateTime.now())
    val coin = integer("coin").default(0)
    val timesPlayed = integer("times_played").default(0)
    val humanKills = integer("human_kills").default(0)
    val monsterKills = integer("monster_kills").default(0)
    val gunKills   = integer("gun_kills").default(0)
    val meleeKills = integer("melee_kills").default(0)
    val online = bool("online").default(false)
}