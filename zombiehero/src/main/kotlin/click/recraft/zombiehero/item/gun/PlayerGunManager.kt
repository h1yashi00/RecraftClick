package click.recraft.zombiehero.item.gun

import click.recraft.zombiehero.item.CustomItem
import click.recraft.zombiehero.item.CustomItemManager
import java.util.*

class PlayerGunManager() : CustomItemManager {
    // save item uuid
    override val save: HashMap<UUID, CustomItem> = hashMapOf()
}