package click.recraft.share.database.table

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column
import java.util.*

object TableUserOption: IdTable<UUID>("user_option") {
    override val id: Column<EntityID<UUID>> = uuid("player_uuid").entityId()
    val autoLoadResourcePack = bool("auto_load_resource_pack").default(true)
    val itemMain = reference("item_main",   TableItem.id)
    val itemSub = reference("item_sub",     TableItem.id)
    val itemMelee = reference("item_melee", TableItem.id)
    val itemSkill = reference("item_skill", TableItem.id)
}