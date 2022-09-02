package click.recraft.zombiehero.gun.api

import click.recraft.zombiehero.item.gun.Gun
import org.bukkit.Sound
import org.bukkit.entity.Player

class ReloadFullBullet(
    override var armo : Int = 0,
    override var reloadTime: Tick = Tick.sec(0.0),
    override val reloadManager: ReloadManager,
) : Reload {
    override fun reload(player: Player, gun: Gun) {
        if (!check(gun)) {
            return
        }
        reloadManager.register(gun, player)
        player.playSound(player.location, Sound.BLOCK_WOODEN_DOOR_OPEN, 1f,2f)
    }
}
