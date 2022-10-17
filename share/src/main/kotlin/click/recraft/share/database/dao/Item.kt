package click.recraft.share.database.dao

import click.recraft.share.database.table.TableItem
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.id.EntityID

class Item(id: EntityID<Int>): IntEntity(id) {
    companion object : EntityClass<Int, Item>(TableItem)
    var name by TableItem.name
    var price by TableItem.price
}