package click.recraft.zombiehero.item.gun

import click.recraft.zombiehero.ZombieHero
import click.recraft.zombiehero.gun.api.Accuracy
import click.recraft.zombiehero.gun.api.OneShot
import click.recraft.zombiehero.gun.api.ReloadFullBullet
import click.recraft.zombiehero.gun.api.Tick
import org.bukkit.Material

class HandGun: Gun(
    material = Material.PINK_DYE,
    name = "HandGun",
    customModeValue = 30,
    shootManager = ZombieHero.plugin.shootManager,
    reload = ReloadFullBullet (
        7,
        Tick.sec(1.3),
        ZombieHero.plugin.reloadManager
    ),
    shot = OneShot(
        Tick.sec(0.7),
        70,
        1.0,
        Accuracy(0),
        Tick.sec(0.5)
    ),
    walkSpeed = 5
)