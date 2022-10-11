package click.recraft.zombiehero.item

import click.recraft.share.protocol.TextureItem
import click.recraft.zombiehero.gun.api.GameSound
import click.recraft.zombiehero.gun.api.Tick
import click.recraft.zombiehero.item.grenade.ZombieBomb
import click.recraft.zombiehero.item.grenade.HitGrenade
import click.recraft.zombiehero.item.grenade.NormalGrenade
import click.recraft.zombiehero.item.grenade.ZombieGrenadeTouch
import click.recraft.zombiehero.item.gun.*
import click.recraft.zombiehero.item.melee.Melee
import click.recraft.zombiehero.item.skill.AmmoBox
import click.recraft.zombiehero.item.skill.HeadShot
import click.recraft.zombiehero.item.skill.SpeedUp
import java.util.*
import kotlin.collections.HashMap

class CustomItemFactory : CustomItemManager {
    override val save: HashMap<UUID, CustomItem> = hashMapOf()

    enum class MainGunType {
        AK47,
        SHOTGUN,
        AWP,
        MP5,
        MOSIN,
        Saiga,
    }
    enum class SubGunType {
        DesertEagle,
        Glock,
    }
    enum class GrenadeType {
        HITGrenade,
        NormalGrenade
    }
    enum class ZombieItem {
        ZombieGrenadeTouch,
        ZombieBomb,
    }
    enum class MeleeType {
        NATA,
        Hammer,
    }

    enum class SkillType {
        SPEED_UP,
        HEADSHOT,
        AMMO_BOX,
    }

    fun createMainGun (
        type: MainGunType
    ): Gun {
        val gun = when(type) {
            MainGunType.AK47 -> AK47()
            MainGunType.SHOTGUN -> M870()
            MainGunType.AWP  -> Awp()
            MainGunType.MP5 -> Mp5()
            MainGunType.MOSIN -> Mosin()
            MainGunType.Saiga -> Saiga()
        }
        register(gun.unique, gun)
        return gun
    }
    fun createSubGun(
        type: SubGunType
    ): Gun {
        val gun = when(type) {
            SubGunType.DesertEagle -> DesertEagle()
            SubGunType.Glock -> Glock()
        }
        register(gun.unique, gun)
        return gun
    }
    fun createMelee(meleeType: MeleeType): Melee {
        val melee = when(meleeType) {
            MeleeType.NATA -> Melee(
                    "Nata",
                 500,
                    Tick.sec(1.0),
                    0.5,
                    TextureItem.MELEE_NATA,
                -10,
                GameSound(GameSound.Type.SWING_SMALL)
                )
            MeleeType.Hammer -> Melee(
                "Hammer",
                700,
                Tick.sec(1.5),
                3.0,
                TextureItem.MELEE_HAMMER,
                -150,
                GameSound(GameSound.Type.SWING_BIG)
            )
        }
        register(melee.unique, melee)
        return melee
    }
    fun createSkillItem(skillType: SkillType): CustomItem {
        val skill = when (skillType) {
            SkillType.SPEED_UP -> SpeedUp()
            SkillType.HEADSHOT -> HeadShot()
            SkillType.AMMO_BOX -> AmmoBox()
        }
        register(skill.unique, skill)
        return skill
    }
    fun createGrenade(grenadeType: GrenadeType): CustomItem {
        val grenade = when(grenadeType) {
            GrenadeType.HITGrenade   -> HitGrenade("hitGrenade")
            GrenadeType.NormalGrenade -> NormalGrenade()
        }
        register(grenade.unique, grenade)
        return grenade
    }
    fun createZombieItem(zombieItem: ZombieItem): CustomItem {
        val grenade = when(zombieItem) {
            ZombieItem.ZombieGrenadeTouch -> ZombieGrenadeTouch()
            ZombieItem.ZombieBomb -> ZombieBomb()
        }
        register(grenade.unique, grenade)
        return grenade
    }
}