package click.recraft.zombiehero.gun.api

class GameSound(val type: Type, val volume: Float = 1F, val pitch: Float = 1f){
    enum class Type(val sound: String) {
        AK47_SHOT         ("minecraft:ak47_shot"),
        AK47_RELOAD       ("minecraft:ak47_reload"),
        AK47_RELOAD_FINISH("minecraft:ak47_reload_finish"),
        MOSIN_SHOT        ("minecraft:mosin_shot"),
        MOSIN_RELOAD      ("minecraft:mosin_reload"),
        MOSIN_RELOAD_FINISH("minecraft:mosin_reload_finish"),
        SHOTGUN_SHOT      ("minecraft:shotgun_shot"),
        SHOTGUN_RELOAD    ("minecraft:shotgun_reload"),
        SHOTGUN_RELOAD_FINISH("minecraft:shotgun_reload_finish"),
        SWING_BIG("minecraft:swing_big"),
        SWING_SMALL("minecraft:swing_small"),
        HORROR_MONSTER_SCREAM("horror_monster_scream")
    }
}