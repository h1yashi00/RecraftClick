package click.recraft.share.database.table

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column
import java.util.*

object TableUserItem: IdTable<UUID>("user_item_unlocked") {
    override val id: Column<EntityID<UUID>> = uuid("player_uuid").entityId()
    val main_ak47       = bool("main_ak47").default(true)
    val main_mp5        = bool("main_mp5").default(false)
    val main_awp        = bool("main_awp").default(false)
    val main_saiga      = bool("main_saiga").default(false)
    val main_m870       = bool("main_m870").default(false)
    val main_mosin      = bool("main_mosin").default(false)
    val sub_desert_eagle= bool("sub_desert_eagle").default(true)
    val sub_glock       = bool("sub_sub_glock").default(false)
    val melee_nata      = bool("melee_nata").default(true)
    val melee_hammer    = bool("melee_hammer").default(false)
    val skill_ammo_dump = bool("skill_ammo_dump").default(true)
    val skill_grenade   = bool("skill_ammo_grenade").default(false)
    val skill_zombie_grenade = bool("skill_zombie_grenade").default(false)
    val skill_zombie_grenade_touch = bool("skill_zombie_grenade_touch").default(false)
}