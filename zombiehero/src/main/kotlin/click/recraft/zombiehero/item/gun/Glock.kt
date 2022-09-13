package click.recraft.zombiehero.item.gun

import click.recraft.zombiehero.ZombieHero
import click.recraft.zombiehero.gun.api.*

class Glock : Gun(
    customModeValue = 1000008,
    shootManager = ZombieHero.plugin.shootManager,
    reload = Reload (
        15,
        Tick.sec(1.0),
        ZombieHero.plugin.reloadManagerFullBullet,
    ),
    shot = OneShot (
        Tick.sec(0.05),
        damage = 300,
        knockBack = 1.3,
        Accuracy(0),
        Tick.sec(0.0),
    ),
    name = "Glock",
    walkSpeed = 30,
    shotSound = GameSound(GameSound.Type.AK47_SHOT),
    reloadSound = GameSound(GameSound.Type.AK47_RELOAD),
    reloadFinishSound = GameSound(GameSound.Type.AK47_RELOAD_FINISH)
){
}