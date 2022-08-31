package click.recraft.zombiehero.player

import click.recraft.zombiehero.ZombieHero
import click.recraft.zombiehero.task.OneTickTimerTask
import org.bukkit.Bukkit

object WalkSpeedManager: OneTickTimerTask {
    override fun loopEveryOneTick() {
        Bukkit.getOnlinePlayers().forEach { player ->
            // どのアイテム化識別して､正しいwalkingSpeedを割り当てる
            val item = player.inventory.itemInMainHand
            val customItem = ZombieHero.plugin.customItemFactory.getItem(item)
            var walkSpeed = 0.20F
            if (customItem is WalkSpeed) {
                walkSpeed += customItem.walkSpeed
            }
            player.walkSpeed = walkSpeed
        }
    }
}