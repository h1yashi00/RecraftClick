package click.recraft.zombiehero.item.gun

import click.recraft.share.protocol.TextureItem
import click.recraft.zombiehero.ZombieHero
import click.recraft.zombiehero.gun.api.*

class Saiga: Gun(
    name = "Saiga",
    textureItem = TextureItem.MAIN_SAIGA,
    shootManager = ZombieHero.plugin.shootManager,
    reload = Reload (
        8,
        Tick.sec(1.5),
        ZombieHero.plugin.reloadManagerFullBullet,
    ),
    shot = MultiShot (
        rate = Tick.sec(0.3),
        40,
        4,
        0.3,
        Accuracy(200,30),
        Tick.sec(0.05)
    ),
    walkSpeed = 5,
    shotSound = GameSound(GameSound.Type.SHOTGUN_SHOT),
    reloadSound = GameSound(GameSound.Type.AK47_RELOAD),
    reloadFinishSound = GameSound(GameSound.Type.AK47_RELOAD_FINISH),
    recoilY = 5f,
    recoilX = 0f,
)