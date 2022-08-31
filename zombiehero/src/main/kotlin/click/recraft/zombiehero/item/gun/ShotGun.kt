package click.recraft.zombiehero.item.gun

import click.recraft.zombiehero.ZombieHero
import click.recraft.zombiehero.gun.api.Accuracy
import click.recraft.zombiehero.gun.api.MultiShot
import click.recraft.zombiehero.gun.api.ReloadFullBullet
import click.recraft.zombiehero.gun.api.Tick
import org.bukkit.Material

class ShotGun: Gun(
    material = Material.PINK_DYE,
    name = "ShotGun",
    customModeValue = 30,
    shootManager = ZombieHero.plugin.shootManager,
    reload = ReloadFullBullet (
        8,
        Tick.sec(0.5),
        ZombieHero.plugin.reloadManager
    ),
    shot = MultiShot (
        rate = Tick.sec(0.5),
        10,
        5,
        0.3,
        Accuracy(200),
        Tick.sec(0.05)
    ),
    walkSpeed = -0.05F
) {
    override fun getReloadTime(): Int {
        return super.getReloadTime()
    }
}