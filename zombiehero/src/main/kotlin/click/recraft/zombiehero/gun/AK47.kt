package click.recraft.zombiehero.gun

import click.recraft.zombiehero.gun.base.scope.GunScope
import click.recraft.zombiehero.gun.base.scope.ScopeMagnification
import org.bukkit.Material

class AK47: GunScope(
    Material.SHEARS,
    1,
    100000,
    200,
    "AK-47",
    2000,
    100.0,
    300000,
    reloadTime = 1000, // 1sec
    spread = 0.05,
    recoilZ = -0.1F,
    recoilY = 0.3F,
    bulletRange = 30,
    magnification = ScopeMagnification.SIX
//    walkingSpeed = 0.2F
//    shootArmoNum = 5
)