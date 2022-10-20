package click.recraft.share.database.table

import click.recraft.share.database.dao.DailyQuest
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column
import java.util.*

object TableUserDailyQuest: IdTable<UUID>("user_daily_quest") {
    override val id: Column<EntityID<UUID>> = uuid("daily_quest_uuid").entityId()
    val dailyQuest1       = reference("daily_quest_1", TableDailyQuest.id).default(DailyQuest.findById(1)!!.id)
    val dailyQuest1Received = bool("daily_quest_1_received").default(false)
    val dailyQuest1Counter       = integer("daily_quest_1_counter").default(0)
    val dailyQuest1Finished = bool("daily_quest_1_finished").default(false)

    val dailyQuest2 = reference("daily_quest_2", TableDailyQuest.id).default(DailyQuest.findById(2)!!.id)
    val dailyQuest2Received = bool("daily_quest_2_received").default(false)
    val dailyQuest2Counter       = integer("daily_quest_2_counter").default(0)
    val dailyQuest2Finished = bool("daily_quest_2_finished").default(false)

    val dailyQuest3 = reference("daily_quest_3", TableDailyQuest.id).default(DailyQuest.findById(3)!!.id)
    val dailyQuest3Received = bool("daily_quest_3_received").default(false)
    val dailyQuest3Counter       = integer("daily_quest_3_counter").default(0)
    val dailyQuest3Finished = bool("daily_quest_3_finished").default(false)
}