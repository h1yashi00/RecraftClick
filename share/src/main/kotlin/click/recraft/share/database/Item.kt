package click.recraft.share.database

import click.recraft.share.database.dao.UserOption

enum class Item(val id: Int, val price: Int) {
    MAIN_AK47    (1, 1000),
    MAIN_AWP     (2, 1000),
    MAIN_SAIGA   (3, 1000),
    MAIN_M870    (4, 1000),
    MAIN_MP5     (5, 1000),
    MAIN_MOSIN   (6, 1000),
    SUB_DESERT_EAGLE(7, 1000),
    SUB_GLOCK   (8,  1000),
    MELEE_NATA  (9, 10000),
    MELEE_HAMMER(10, 1000),
    SKILL_AMMO_DUMP(11, 10000),
    SKILL_GRENADE(12, 1000),
    SKILL_ZOMBIE_GRENADE(13, 1000),
    SKILL_ZOMBIE_GRENADE_TOUCH(14, 1000);
    companion object {
        fun getMain(): List<Item> {
            return values().filter { it.name.startsWith("MAIN_") }
        }
        fun getSub(): List<Item> {
            return values().filter { it.name.startsWith("SUB_") }
        }
        fun getMelee(): List<Item> {
            return values().filter { it.name.startsWith("MELEE_") }
        }
        fun getSkill(): List<Item> {
            return values().filter { it.name.startsWith("SKILL_") }
        }
        fun getMainById(option: UserOption): Item {
            return getMain().firstOrNull { it.id == option.itemMain.value } ?: MAIN_AK47
        }
        fun getSubById(option: UserOption): Item {
            return getSub().firstOrNull {it.id == option.itemSub.value} ?: SUB_DESERT_EAGLE
        }
        fun getMeleeById(option: UserOption): Item {
            return getMelee().firstOrNull{it.id == option.itemMelee.value} ?: MELEE_NATA
        }
        fun getSkillById(option: UserOption): Item {
            return getSkill().firstOrNull {it.id == option.itemSkill.value} ?: SKILL_AMMO_DUMP
        }
    }
}