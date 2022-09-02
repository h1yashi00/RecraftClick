package click.recraft.zombiehero.item.gun

import click.recraft.zombiehero.ZombieHero
import click.recraft.zombiehero.gun.api.*
import org.bukkit.Material

class Awp : GunScope (
    Material.BLACK_DYE,
    "Awp",
    42394,
    shootManager = ZombieHero.plugin.shootManager,
    reload = ReloadFullBullet(
        7,
        Tick.sec(1.5),
        ZombieHero.plugin.reloadManager,
    ),
    shot = OneShot (
        Tick.sec(1.4),
        damage = 500,
        knockBack = 1.3,
        Accuracy(0),
        Tick.sec(0.0),
    ),
    -30
) {
}