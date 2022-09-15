package click.recraft.zombiehero.item.gun

import click.recraft.zombiehero.ZombieHero
import click.recraft.zombiehero.gun.api.*

class Mosin : Gun(
    customModeValue = 11,
    shootManager = ZombieHero.plugin.shootManager,
    reload = Reload (
        7,
        Tick.sec(0.7),
        ZombieHero.plugin.oneBulletReloadManager,
    ),
    shot = OneShot (
        Tick.sec(1.0),
        damage = 300,
        knockBack = 1.3,
        Accuracy(0),
        Tick.sec(0.0),
    ),
    name = "MosinNagant",
    walkSpeed = 30,
    shotSound = GameSound(GameSound.Type.MOSIN_SHOT),
    reloadSound = GameSound(GameSound.Type.MOSIN_RELOAD),
    reloadFinishSound = GameSound(GameSound.Type.MOSIN_RELOAD_FINISH),
    recoilX = 10f,
    recoilY = 10f,
)