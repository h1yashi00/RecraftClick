package click.recraft.zombiehero.gun.api

import click.recraft.zombiehero.gun.base.Tick

class ReloadOneBullet (
    override val armo: Int,
    override val reloadTime: Tick
) : Reload {
}