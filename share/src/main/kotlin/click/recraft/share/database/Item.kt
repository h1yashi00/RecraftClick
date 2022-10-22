package click.recraft.share.database

enum class Item(val id: Int, val price: Int) {
    MAIN_AK47    (1, 0),
    MAIN_AWP     (2, 10000),
    MAIN_SAIGA   (3, 10000),
    MAIN_M870    (4, 5000),
    MAIN_MP5     (5, 5000),
    MAIN_MOSIN   (6, 5000),
    SUB_DESERT_EAGLE(7, 0),
    SUB_GLOCK   (8,  3000),
    MELEE_NATA  (9, 0),
    MELEE_HAMMER(10, 10000),
    SKILL_AMMO_DUMP(11, 0),
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
        fun getMainById(id: Int): Item {
            return getMain().firstOrNull { it.id == id } ?: MAIN_AK47
        }
        fun getSubById(id: Int): Item {
            return getSub().firstOrNull {it.id == id} ?: SUB_DESERT_EAGLE
        }
        fun getMeleeById(id: Int): Item {
            return getMelee().firstOrNull{it.id == id} ?: MELEE_NATA
        }
        fun getSkillById(id: Int): Item {
            return getSkill().firstOrNull {it.id == id} ?: SKILL_AMMO_DUMP
        }
    }
}