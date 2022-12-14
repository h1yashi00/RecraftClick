package click.recraft.zombiehero.item.gun

import click.recraft.share.protocol.TextureItem
import click.recraft.zombiehero.ZombieHero
import click.recraft.zombiehero.gun.api.*

class Awp : Gun (
    "Awp",
    textureItem = TextureItem.MAIN_AWP,
    shootManager = ZombieHero.plugin.shootManager,
    reload = Reload (
        7,
        Tick.sec(3.0),
        ZombieHero.plugin.reloadManagerFullBullet,
    ),
    shot = OneShot (
        Tick.sec(1.4),
        damage = 500,
        knockBack = 1.3,
        Accuracy(30,0),
        Tick.sec(0.0),
    ),
    walkSpeed = -30,
    shotSound = GameSound(GameSound.Type.MOSIN_SHOT, 1F, 0.7F),
    reloadSound = GameSound(GameSound.Type.MOSIN_RELOAD,1F,0.7F),
    reloadFinishSound = GameSound(GameSound.Type.MOSIN_RELOAD_FINISH,1F,0.7F),
    recoilX = 10f,
    recoilY = 10f,
)