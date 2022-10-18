package click.recraft.share.database.dao

import click.recraft.share.database.table.TableUser
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

class User(id: EntityID<UUID>): Entity<UUID>(id) {
    companion object : EntityClass<UUID, User>(TableUser)
    var name by TableUser.name
    val firstLogin by TableUser.firstLogin
    var lastLogin  by TableUser.lastLogin
    var lastLogout by TableUser.lastLogout
    var coin: Int by TableUser.coin
    var timesPlayed: Int by TableUser.timesPlayed
    var humanKills: Int by TableUser.humanKills
    var monsterKills: Int by TableUser.monsterKills
    var gunKills: Int by TableUser.gunKills
    var meleeKills: Int by TableUser.meleeKills
    var online: Boolean by TableUser.online
}