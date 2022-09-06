package click.recraft.zombiehero.item.gun

import click.recraft.zombiehero.ZombieHero
import click.recraft.zombiehero.gun.api.*
import org.bukkit.Material

class Awp : Gun (
    Material.BLACK_DYE,
    "Awp",
    customModeValue = 1000002,
    shootManager = ZombieHero.plugin.shootManager,
    reload = Reload (
        7,
        Tick.sec(1.5),
        ZombieHero.plugin.reloadManagerFullBullet,
    ),
    shot = OneShot (
        Tick.sec(1.4),
        damage = 500,
        knockBack = 1.3,
        Accuracy(0),
        Tick.sec(0.0),
    ),
    -30
)