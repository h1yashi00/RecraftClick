package click.recraft.zombiehero.item.gun

import click.recraft.zombiehero.ZombieHero
import click.recraft.zombiehero.gun.api.*
import org.bukkit.Material

class Mp5: Gun(
    material = Material.BLACK_DYE,
    name = "Mp5",
    customModeValue = 1000005,
    shootManager = ZombieHero.plugin.shootManager,
    reload = Reload (
        30,
        Tick.sec(1.0),
        ZombieHero.plugin.reloadManagerFullBullet,
    ),
    shot = OneShot (
        rate = Tick.sec(0.1),
        8,
        0.3,
        Accuracy(30),
        Tick.sec(0.0)
    ),
    walkSpeed = 0
) {
}