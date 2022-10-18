package click.recraft.share.database.table

import org.jetbrains.exposed.dao.id.IntIdTable

object TableItem: IntIdTable("item") {
    val name = varchar("item_name", 30)
}