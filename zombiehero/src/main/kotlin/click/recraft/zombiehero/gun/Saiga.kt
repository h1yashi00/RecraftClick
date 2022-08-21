package click.recraft.zombiehero.gun

import click.recraft.zombiehero.gun.base.ShotGun
import org.bukkit.Material

class Saiga: ShotGun(
    Material.PINK_DYE,
    customModelData = 25543,
    fireRate = 200,
    name = "Saiga",
    knockBack = 3,
    maxArmo = 30,
    reloadTime = 400,
    spread = 0.1,
    shootArmoNum = 5,
    bulletRange = 35,
    recoilZ = 0F,
    recoilY = 0F,
    damage = 5.0
) {
}