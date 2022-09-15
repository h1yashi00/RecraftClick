package click.recraft.zombiehero.item

import click.recraft.zombiehero.ZombieHero
import click.recraft.zombiehero.gun.api.GameSound
import click.recraft.zombiehero.gun.api.Tick
import click.recraft.zombiehero.item.grenade.ZombieBomb
import click.recraft.zombiehero.item.grenade.HitGrenade
import click.recraft.zombiehero.item.grenade.NormalGrenade
import click.recraft.zombiehero.item.grenade.ZombieGrenadeTouch
import click.recraft.zombiehero.item.gun.*
import click.recraft.zombiehero.item.melee.Sword
import java.util.*
import kotlin.collections.HashMap

class CustomItemFactory : CustomItemManager {
    override val save: HashMap<UUID, CustomItem> = hashMapOf()

    enum class GunType {
        AK47,
        SHOTGUN,
        DesertEagle,
        AWP,
        MP5,
        MOSIN,
        Glock,
        Saiga,
    }
    enum class GrenadeType {
        ZombieGrenadeTouch,
        ZombieBomb,
        HITGrenade,
        NormalGrenade
    }
    enum class SwordType {
        NORMAL_SWORD,
        BIG_SWORD,
    }

    fun createGun (
        type: GunType
    ): Gun {
        val gun = when(type) {
            GunType.AK47 -> AK47()
            GunType.SHOTGUN -> M870()
            GunType.DesertEagle -> DesertEagle()
            GunType.AWP  -> Awp()
            GunType.MP5 -> Mp5()
            GunType.MOSIN -> Mosin()
            GunType.Glock -> Glock()
            GunType.Saiga -> Saiga()
        }
        register(gun.unique, gun)
        return gun
    }
    fun createSword(swordType: SwordType): Sword {
        val sword = when(swordType) {
            SwordType.NORMAL_SWORD -> Sword(
                    "nata",
                    ZombieHero.plugin.meleeCoolDownManager,
                    100,
                    Tick.sec(1.0),
                    1,
                -10,
                GameSound(GameSound.Type.SWING_SMALL)
                )
            SwordType.BIG_SWORD -> Sword(
                "hummer",
                ZombieHero.plugin.meleeCoolDownManager,
                10,
                Tick.sec(0.3),
                2,
                200,
                GameSound(GameSound.Type.SWING_BIG)
            )
        }
        register(sword.unique, sword)
        return sword
    }
    fun createGrenade(grenadeType: GrenadeType): CustomItem {
        val grenade = when(grenadeType) {
            GrenadeType.ZombieGrenadeTouch -> ZombieGrenadeTouch()
            GrenadeType.ZombieBomb -> ZombieBomb()
            GrenadeType.HITGrenade   -> HitGrenade("hitGrenade")
            GrenadeType.NormalGrenade -> NormalGrenade()
        }
        register(grenade.unique, grenade)
        return grenade
    }
}