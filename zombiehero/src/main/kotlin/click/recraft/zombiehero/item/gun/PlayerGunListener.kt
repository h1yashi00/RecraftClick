package click.recraft.zombiehero.item.gun

import click.recraft.zombiehero.Util
import click.recraft.zombiehero.ZombieHero
import click.recraft.zombiehero.item.CustomItemListener
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerItemHeldEvent

class PlayerGunListener(
    override val manager: PlayerGunManager
) : CustomItemListener {
    @EventHandler
    fun swap(event: PlayerItemHeldEvent) {
        val player = event.player
        event.newSlot
        val item = player.inventory.getItem(event.newSlot) ?: return
        if (item.type.isAir) {
            return
        }
        val customItem = getItem(item) ?: return
        (customItem as PlayerGun).playerGiveItem(player)
    }
    @EventHandler
    override fun itemDrop(e: PlayerDropItemEvent) {
        val player = e.player
        val item = getItem(e.itemDrop.itemStack) ?: return
        if (!item.isDroppable()) {
            e.isCancelled = true
        }
        val task = Util.createTask {
            (item as PlayerGun).reload(player)
        }
        Bukkit.getScheduler().runTaskLater(ZombieHero.plugin, task, 1)
    }
}