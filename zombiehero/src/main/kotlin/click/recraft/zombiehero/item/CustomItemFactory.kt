package click.recraft.zombiehero.item

import click.recraft.share.database.Item
import click.recraft.share.database.Item.*
import click.recraft.share.protocol.TextureItem
import click.recraft.zombiehero.gun.api.GameSound
import click.recraft.zombiehero.gun.api.Tick
import click.recraft.zombiehero.item.gun.*
import click.recraft.zombiehero.item.melee.Melee
import click.recraft.zombiehero.item.skill.AmmoBox
import click.recraft.zombiehero.item.skill.grenade.NormalGrenade
import click.recraft.zombiehero.item.skill.grenade.ZombieBomb
import click.recraft.zombiehero.item.skill.grenade.ZombieGrenadeTouch
import java.util.*
import kotlin.collections.HashMap

class CustomItemFactory : CustomItemManager {
    override val save: HashMap<UUID, CustomItem> = hashMapOf()

    fun create(
        type: Item
    ): CustomItem {
        val customItem = when(type) {
            MAIN_AK47 -> AK47()
            MAIN_MP5 -> Mp5()
            MAIN_M870 -> M870()
            MAIN_SAIGA -> Saiga()
            MAIN_MOSIN -> Mosin()
            MAIN_AWP -> Awp()
            SUB_GLOCK -> Glock()
            SUB_DESERT_EAGLE -> DesertEagle()
            MELEE_NATA -> Melee(
                "Nata",
                500,
                Tick.sec(1.0),
                0.5,
                TextureItem.MELEE_NATA,
                -10,
                GameSound(GameSound.Type.SWING_SMALL)
            )
            MELEE_HAMMER -> Melee(
                "Hammer",
                700,
                Tick.sec(1.5),
                3.0,
                TextureItem.MELEE_HAMMER,
                -150,
                GameSound(GameSound.Type.SWING_BIG)
            )
            SKILL_AMMO_DUMP -> AmmoBox()
            SKILL_ZOMBIE_GRENADE -> ZombieBomb()
            SKILL_ZOMBIE_GRENADE_TOUCH-> ZombieGrenadeTouch()
            SKILL_GRENADE -> NormalGrenade()
        }
        register(customItem.unique, customItem)
        return customItem
    }
}