package click.recraft.share.database.dao

import click.recraft.share.database.table.TableUserOption
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

class UserOption(id: EntityID<UUID>): Entity<UUID>(id) {
    companion object : EntityClass<UUID, UserOption>(TableUserOption)
    var autoLoadResourcePack by   TableUserOption.autoLoadResourcePack
    var itemMain by     TableUserOption.itemMain
    var itemSub by      TableUserOption.itemSub
    var itemMelee by    TableUserOption.itemMelee
    var itemSkill by    TableUserOption.itemSkill
}