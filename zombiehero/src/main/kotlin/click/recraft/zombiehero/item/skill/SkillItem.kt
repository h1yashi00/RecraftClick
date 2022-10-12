package click.recraft.zombiehero.item.skill

import click.recraft.share.item
import click.recraft.zombiehero.item.CustomItem
import org.bukkit.Material
import java.util.*

abstract class SkillItem(
    material: Material,
    description: ArrayList<String>,
    displayName: String,
    customModelData: Int = 0
): CustomItem(
    item(
        material,
        localizedName = UUID.randomUUID().toString(),
        lore = description,
        displayName = displayName,
        customModelData = customModelData
    )
)