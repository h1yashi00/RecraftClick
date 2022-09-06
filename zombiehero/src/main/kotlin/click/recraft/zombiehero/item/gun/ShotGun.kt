package click.recraft.zombiehero.item.gun

import click.recraft.zombiehero.ZombieHero
import click.recraft.zombiehero.gun.api.*
import org.bukkit.Material

class ShotGun: Gun(
    material = Material.PINK_DYE,
    name = "ShotGun",
    customModeValue = 10,
    shootManager = ZombieHero.plugin.shootManager,
    reload = Reload (
        8,
        Tick.sec(0.5),
        ZombieHero.plugin.oneBulletReloadManager,
    ),
    shot = MultiShot (
        rate = Tick.sec(0.5),
        10,
        5,
        0.3,
        Accuracy(200),
        Tick.sec(0.05)
    ),
    walkSpeed = 5
) {
}