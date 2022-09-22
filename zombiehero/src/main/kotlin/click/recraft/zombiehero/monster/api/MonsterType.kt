package click.recraft.zombiehero.monster.api

import click.recraft.share.item
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

enum class MonsterType(
    val head: ItemStack
) {
    ZOMBIE(item(Material.ZOMBIE_HEAD)),
    SKELETON(item(Material.SKELETON_SKULL))
}