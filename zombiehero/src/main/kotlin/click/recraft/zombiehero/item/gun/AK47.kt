package click.recraft.zombiehero.item.gun

import click.recraft.zombiehero.ZombieHero
import click.recraft.zombiehero.gun.api.OneShot
import click.recraft.zombiehero.gun.api.ReloadFullBullet
import click.recraft.zombiehero.gun.api.Tick
import org.bukkit.Material

class AK47: Gun(
    material = Material.STICK,
    name = "AK-47",
    customModeValue = 30,
    shootManager = ZombieHero.plugin.shootManager,
    reload = ReloadFullBullet (
        30,
        Tick.sec(1.0),
        ZombieHero.plugin.reloadManager
    ),
    shot = OneShot (
        Tick.sec(0.5),
        10,
        0.2,
        0.1,
        0.0F,
        0.0F,
        Tick.sec(0.3)
    ),
    walkSpeed = 0.25F
) {
}