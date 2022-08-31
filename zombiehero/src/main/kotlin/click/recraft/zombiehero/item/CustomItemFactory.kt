package click.recraft.zombiehero.item

import click.recraft.zombiehero.ZombieHero
import click.recraft.zombiehero.gun.api.Tick
import click.recraft.zombiehero.item.grenade.GrenadeImpl
import click.recraft.zombiehero.item.grenade.HitGrenade
import click.recraft.zombiehero.item.grenade.TouchGrenadeImpl
import click.recraft.zombiehero.item.grenade.WtfGrenade
import click.recraft.zombiehero.item.gun.AK47
import click.recraft.zombiehero.item.gun.Gun
import click.recraft.zombiehero.item.gun.HandGun
import click.recraft.zombiehero.item.gun.ShotGun
import click.recraft.zombiehero.item.melee.Sword
import java.util.*
import kotlin.collections.HashMap

class CustomItemFactory : CustomItemManager {
    override val save: HashMap<UUID, CustomItem> = hashMapOf()

    enum class GunType {
        AK47,
        SHOTGUN,
        HANDGUN
    }
    enum class GrenadeType {
        TouchGrenade,
        Grenade,
        HITGrenade,
        WtfGrenade
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
            GunType.SHOTGUN -> ShotGun()
            GunType.HANDGUN -> HandGun()
        }
        register(gun.unique, gun)
        return gun
    }
    fun createSword(swordType: SwordType): Sword {
        val sword = when(swordType) {
            SwordType.NORMAL_SWORD -> Sword(
                    ZombieHero.plugin.meleeCoolDownManager,
                    100,
                    Tick.sec(1.0)
                )
            SwordType.BIG_SWORD -> Sword(
                ZombieHero.plugin.meleeCoolDownManager,
                10,
                Tick.sec(0.3),
            )
        }
        register(sword.unique, sword)
        return sword
    }
    fun createGrenade(grenadeType: GrenadeType): CustomItem {
        val grenade = when(grenadeType) {
            GrenadeType.TouchGrenade -> TouchGrenadeImpl()
            GrenadeType.Grenade      -> GrenadeImpl()
            GrenadeType.HITGrenade   -> HitGrenade("hitGrenade")
            GrenadeType.WtfGrenade   -> WtfGrenade()
        }
        register(grenade.unique, grenade)
        return grenade
    }
}