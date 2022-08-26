package click.recraft.zombiehero

import click.recraft.zombiehero.item.CustomItem
import click.recraft.zombiehero.item.CustomItemManager
import java.util.*

class TouchGrenadeManager: CustomItemManager {
    override val save: HashMap<UUID, CustomItem> = hashMapOf()
}
