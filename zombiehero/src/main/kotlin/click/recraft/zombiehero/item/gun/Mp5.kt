package click.recraft.zombiehero.item.gun

import click.recraft.share.protocol.TextureItem
import click.recraft.zombiehero.ZombieHero
import click.recraft.zombiehero.gun.api.*

class Mp5: Gun(
    name = "Mp5",
    textureItem = TextureItem.MAIN_MP5,
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
        Accuracy(100,0),
        Tick.sec(0.0)
    ),
    walkSpeed = 0,
    shotSound = GameSound(GameSound.Type.AK47_SHOT),
    reloadSound = GameSound(GameSound.Type.AK47_RELOAD),
    reloadFinishSound = GameSound(GameSound.Type.AK47_RELOAD_FINISH),
    recoilX = 1f,
    recoilY = 1f,
) {
}