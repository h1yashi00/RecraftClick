package click.recraft.zombiehero.item.gun

import click.recraft.share.protocol.TextureItem
import click.recraft.zombiehero.ZombieHero
import click.recraft.zombiehero.gun.api.*

class Glock : Gun(
    textureItem = TextureItem.SUB_GLOCK,
    shootManager = ZombieHero.plugin.shootManager,
    reload = Reload (
        15,
        Tick.sec(1.0),
        ZombieHero.plugin.reloadManagerFullBullet,
    ),
    shot = OneShot (
        Tick.sec(0.05),
        damage = 30,
        knockBack = 1.3,
        Accuracy(100,0),
        Tick.sec(0.0),
    ),
    name = "Glock",
    walkSpeed = 30,
    shotSound = GameSound(GameSound.Type.AK47_SHOT),
    reloadSound = GameSound(GameSound.Type.AK47_RELOAD),
    reloadFinishSound = GameSound(GameSound.Type.AK47_RELOAD_FINISH),
    recoilX = 1f,
    recoilY = 2f,
){
}