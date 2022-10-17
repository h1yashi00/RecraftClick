package click.recraft.share.database.dao

import click.recraft.share.database.table.TableUserItem
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

class UserItem(id: EntityID<UUID>): Entity<UUID>(id) {
    companion object: EntityClass<UUID, UserItem>(TableUserItem)
    var mainAk47           by TableUserItem.main_ak47
    var mainMp5            by TableUserItem.main_mp5
    var mainAwp            by TableUserItem.main_awp
    var mainSaiga          by TableUserItem.main_saiga
    var mainM870           by TableUserItem.main_m870
    var mainMosin          by TableUserItem.main_mosin
    var subDesert_eagle    by TableUserItem.sub_desert_eagle
    var subGlock           by TableUserItem.sub_glock
    var meleeNata          by TableUserItem.melee_nata
    var meleeHammer        by TableUserItem.melee_hammer
    var skillAmmo_dump     by TableUserItem.skill_ammo_dump
    var skillGrenade       by TableUserItem.skill_grenade
    var skillZombie_grenade by TableUserItem.skill_zombie_grenade
    var skillZombie_grenade_touch by TableUserItem.skill_zombie_grenade_touch
}