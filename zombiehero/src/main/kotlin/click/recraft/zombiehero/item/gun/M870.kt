package click.recraft.zombiehero.item.gun

import click.recraft.zombiehero.ZombieHero
import click.recraft.zombiehero.gun.api.*

class M870: Gun(
    name = "M870",
    customModeValue = 7,
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
        Accuracy(200,30),
        Tick.sec(0.05)
    ),
    walkSpeed = 5,
    shotSound = GameSound(GameSound.Type.SHOTGUN_SHOT),
    reloadSound = GameSound(GameSound.Type.SHOTGUN_RELOAD),
    reloadFinishSound = GameSound(GameSound.Type.SHOTGUN_RELOAD_FINISH),
    recoilY = 5f,
    recoilX = 5f,
) {
}