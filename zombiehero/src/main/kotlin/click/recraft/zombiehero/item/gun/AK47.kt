package click.recraft.zombiehero.item.gun

import click.recraft.zombiehero.ZombieHero
import click.recraft.zombiehero.gun.api.Accuracy
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
        Tick.sec(1.3),
        ZombieHero.plugin.reloadManager
    ),
    shot = OneShot (
        rate = Tick.sec(0.1),
        10,
        0.2,
        Accuracy(10),
        Tick.sec(0.3)
    ),
    walkSpeed = 5
) {
}