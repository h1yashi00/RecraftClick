package click.recraft.zombiehero.item.gun

import click.recraft.zombiehero.ZombieHero
import click.recraft.zombiehero.gun.api.*

class AK47: Gun(
    name = "AK-47",
    customModeValue = 1,
    shootManager = ZombieHero.plugin.shootManager,
    reload = Reload (
        30,
        Tick.sec(1.3),
        ZombieHero.plugin.reloadManagerFullBullet
    ),
    shot = OneShot (
        rate = Tick.sec(0.15),
        25,
        0.2,
        Accuracy(100, 0),
        Tick.sec(0.3)
    ),
    walkSpeed = 5,
    shotSound = GameSound(GameSound.Type.AK47_SHOT),
    reloadSound = GameSound(GameSound.Type.AK47_RELOAD),
    reloadFinishSound = GameSound(GameSound.Type.AK47_RELOAD_FINISH),
    recoilY = 1f,
    recoilX = 0f,
)