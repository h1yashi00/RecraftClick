package click.recraft.zombiehero.melee

import click.recraft.zombiehero.item.CustomItem
import click.recraft.zombiehero.item.CustomItemManager
import java.util.*

class MeleeManager: CustomItemManager {
    override val save: HashMap<UUID, CustomItem> = hashMapOf()
}