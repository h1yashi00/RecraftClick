package click.recraft.zombiehero.gun.api

import click.recraft.zombiehero.gun.base.Tick

class ReloadFullBullet(
    override var armo : Int = 0,
    override var reloadTime: Tick = Tick.sec(0.0),
) : Reload {
}
