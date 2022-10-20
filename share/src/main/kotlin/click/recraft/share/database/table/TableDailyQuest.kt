package click.recraft.share.database.table

import org.jetbrains.exposed.dao.id.IntIdTable

object TableDailyQuest: IntIdTable("daily_quest") {
    val name = varchar("daily_quest_name", 30)
}