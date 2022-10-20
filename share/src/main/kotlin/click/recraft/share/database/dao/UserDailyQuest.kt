package click.recraft.share.database.dao

import click.recraft.share.database.table.TableUserDailyQuest
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

class UserDailyQuest(id: EntityID<UUID>): Entity<UUID>(id) {
    companion object : EntityClass<UUID, UserDailyQuest>(TableUserDailyQuest)
    val dailyQuest1 by TableUserDailyQuest.dailyQuest1
    val dailyQuest2 by TableUserDailyQuest.dailyQuest2
    val dailyQuest3 by TableUserDailyQuest.dailyQuest3

    var dailyQuestCounter1 : Int by TableUserDailyQuest.dailyQuest1Counter
    var dailyQuestCounter2 : Int by TableUserDailyQuest.dailyQuest2Counter
    var dailyQuestCounter3 : Int by TableUserDailyQuest.dailyQuest3Counter

    var dailyQuestReceived1 : Boolean by TableUserDailyQuest.dailyQuest1Received
    var dailyQuestReceived2 : Boolean by TableUserDailyQuest.dailyQuest2Received
    var dailyQuestReceived3 : Boolean by TableUserDailyQuest.dailyQuest3Received

    var dailyQuestFinished1: Boolean by TableUserDailyQuest.dailyQuest1Finished
    var dailyQuestFinished2: Boolean by TableUserDailyQuest.dailyQuest2Finished
    var dailyQuestFinished3: Boolean by TableUserDailyQuest.dailyQuest3Finished
}