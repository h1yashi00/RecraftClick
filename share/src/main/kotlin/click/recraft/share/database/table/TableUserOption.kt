package click.recraft.share.database.table

import click.recraft.share.database.dao.Item
import click.recraft.share.database.player.Default
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column
import java.util.*

object TableUserOption: IdTable<UUID>("user_option") {
    override val id: Column<EntityID<UUID>> = uuid("player_uuid").entityId()
    val autoLoadResourcePack = bool("auto_load_resource_pack").default(true)
    val itemMain = reference("item_main",   TableItem.id).default(Item.findById(Default.itemMain.id)!!.id)
    val itemSub = reference("item_sub",     TableItem.id).default(Item.findById(Default.itemSub.id)!!.id)
    val itemMelee = reference("item_melee", TableItem.id).default(Item.findById(Default.itemMelee.id)!!.id)
    val itemSkill = reference("item_skill", TableItem.id).default(Item.findById(Default.itemSkill.id)!!.id)
}