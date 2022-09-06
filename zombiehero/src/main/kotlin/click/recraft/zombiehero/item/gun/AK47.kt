package click.recraft.zombiehero.item.gun

import click.recraft.zombiehero.ZombieHero
import click.recraft.zombiehero.gun.api.*
import org.bukkit.Material

class AK47: Gun(
    material = Material.BLACK_DYE,
    name = "AK-47",
    customModeValue = 1000000,
    shootManager = ZombieHero.plugin.shootManager,
    reload = Reload (
        3000,
        Tick.sec(1.3),
        ZombieHero.plugin.reloadManagerFullBullet
    ),
    shot = OneShot (
        rate = Tick.sec(0.05),
        10,
        0.2,
        Accuracy(10),
        Tick.sec(0.3)
    ),
    walkSpeed = 5
)