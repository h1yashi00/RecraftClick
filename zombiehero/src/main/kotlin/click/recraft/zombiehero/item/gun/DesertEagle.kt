package click.recraft.zombiehero.item.gun

import click.recraft.zombiehero.ZombieHero
import click.recraft.zombiehero.gun.api.*

class DesertEagle: Gun(
    name = "DesertEagle",
    customModeValue = 13,
    shootManager = ZombieHero.plugin.shootManager,
    reload = Reload (
        7,
        Tick.sec(1.3),
        ZombieHero.plugin.reloadManagerFullBullet
    ),
    shot = OneShot(
        Tick.sec(0.7),
        100,
        1.0,
        Accuracy(30,0),
        Tick.sec(0.5)
    ),
    walkSpeed = 5,
    shotSound = GameSound(GameSound.Type.AK47_SHOT),
    reloadSound = GameSound(GameSound.Type.AK47_RELOAD),
    reloadFinishSound = GameSound(GameSound.Type.AK47_RELOAD_FINISH),
    recoilX = 3f,
    recoilY = 10f,
)