package click.recraft.share.database.dao

import click.recraft.share.database.table.TableDailyQuest
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.id.EntityID

class DailyQuest(id: EntityID<Int>): IntEntity(id) {
    companion object : EntityClass<Int, DailyQuest>(TableDailyQuest)
    var name by TableDailyQuest.name
}