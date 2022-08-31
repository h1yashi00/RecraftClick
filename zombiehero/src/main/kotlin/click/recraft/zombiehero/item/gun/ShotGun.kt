package click.recraft.zombiehero.item.gun

import click.recraft.zombiehero.ZombieHero
import click.recraft.zombiehero.gun.api.MultiShot
import click.recraft.zombiehero.gun.api.ReloadOneBullet
import click.recraft.zombiehero.gun.api.Tick
import org.bukkit.Material

class ShotGun: Gun(
    material = Material.PINK_DYE,
    name = "ShotGun",
    customModeValue = 30,
    shootManager = ZombieHero.plugin.shootManager,
    reload = ReloadOneBullet (
        8,
        Tick.sec(1.3),
        ZombieHero.plugin.reloadManager
    ),
    shot = MultiShot (
        Tick.sec(0.1),
        10,
        5,
        0.3,
        0.01,
        0.0F,
        0.0F,
        Tick.sec(0.05)
    ),
    walkSpeed = 0.25F
)