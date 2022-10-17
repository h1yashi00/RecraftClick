package click.recraft.share.database

import click.recraft.share.database.dao.User
import click.recraft.share.database.dao.UserItem
import click.recraft.share.database.dao.UserOption
import java.util.*

class UserEntity(uniqueId: UUID, playerName: String) {
    val user = User.findById(uniqueId) ?: User.new(uniqueId) {
        name = playerName
    }

    val item = UserItem.findById(uniqueId) ?: UserItem.new(uniqueId) {}

    val option = UserOption.findById(uniqueId) ?: UserOption.new(uniqueId) {
        val daoItem = click.recraft.share.database.dao.Item
        itemMain    = daoItem.findById(Item.MAIN_AK47.id)!!.id
        itemSub     = daoItem.findById(Item.SUB_DESERT_EAGLE.id)!!.id
        itemMelee   = daoItem.findById(Item.MELEE_NATA.id)!!.id
        itemSkill   = daoItem.findById(Item.SKILL_AMMO_DUMP.id)!!.id
    }
    fun isUnlocked(itemType: Item): Boolean {
        return when(itemType) {
            Item.MAIN_AK47          -> item.mainAk47
            Item.MAIN_MP5           -> item.mainMp5
            Item.MAIN_AWP           -> item.mainAwp
            Item.MAIN_SAIGA         -> item.mainSaiga
            Item.MAIN_M870          -> item.mainM870
            Item.MAIN_MOSIN         -> item.mainMosin
            Item.SUB_DESERT_EAGLE   -> item.subDesert_eagle
            Item.SUB_GLOCK          -> item.subGlock
            Item.MELEE_NATA         -> item.meleeNata
            Item.MELEE_HAMMER       -> item.meleeHammer
            Item.SKILL_AMMO_DUMP    -> item.skillAmmo_dump
            Item.SKILL_GRENADE      -> item.skillGrenade
            Item.SKILL_ZOMBIE_GRENADE -> item.skillZombie_grenade
            Item.SKILL_ZOMBIE_GRENADE_TOUCH -> item.skillZombie_grenade_touch
        }
    }
}