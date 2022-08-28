package click.recraft.zombiehero

import click.recraft.zombiehero.item.CustomItemListener
import click.recraft.zombiehero.melee.MeleeManager

class MeleeListener(
    override val manager: MeleeManager
) : CustomItemListener {
}
