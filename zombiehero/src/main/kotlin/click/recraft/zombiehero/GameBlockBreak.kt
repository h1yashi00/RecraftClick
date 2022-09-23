package click.recraft.zombiehero

import click.recraft.share.item
import click.recraft.zombiehero.item.gun.Gun
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.block.Block
import org.bukkit.entity.Player

enum class GameBlockBreak(
    val type: Material?,
    val match: String? = null,
    val function: (block: Block, player: Player) -> Unit
) {
    LOG(null,  "_log", function = {block, player ->
        MapObjectManager.resourceBlock(block, 20 * 3)
        player.playSound(player, Sound.ENTITY_ITEM_PICKUP, 1f,1f)
        block.type = Material.BEDROCK
        player.inventory.addItem(item(Material.OAK_PLANKS, 4))
    }),
    WOOD(null,  "_wood", function = {block, player ->
        MapObjectManager.resourceBlock(block, 20 * 3)
        player.playSound(player, Sound.ENTITY_ITEM_PICKUP, 1f,1f)
        block.type = org.bukkit.Material.BEDROCK
        player.inventory.addItem(item(org.bukkit.Material.OAK_PLANKS, 4))
    }),
    COAL(Material.COAL_ORE,  function = function@{ block, player ->
        val item = player.inventory.getItem(0)!!
        val customItem = ZombieHero.plugin.customItemFactory.getItem(item) ?: return@function
        if (customItem !is Gun) return@function

        val loc = block.location
        loc.world!!.spawnParticle(org.bukkit.Particle.BLOCK_CRACK, loc, 10, block.type.createBlockData())
        val addArmo = customItem.stats.maxArmo / 3
        player.sendMessage("${org.bukkit.ChatColor.BOLD}${customItem.stats.gunName}の弾を${addArmo}入手した")
        player.playSound(player, Sound.ENTITY_ITEM_PICKUP, 1f,1f)
        customItem.stats.totalArmo += addArmo
        MapObjectManager.resourceBlock(block, 20 * 3)
        block.type = org.bukkit.Material.BEDROCK
    });

    companion object {
        fun get(block: Block): GameBlockBreak? {
            values().forEach {
                if (it.type == null && block.type.toString().endsWith(it.match ?: "fkdsjfdsl4893284", true)) {
                    return it
                }
                if (it.type == block.type) {
                    return it
                }
            }
            return null
        }
    }
}