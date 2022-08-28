package click.recraft.zombiehero.gun.api

import click.recraft.zombiehero.item.gun.PlayerGun
import org.bukkit.Sound
import org.bukkit.entity.Player

class ReloadOneBullet (
    override val armo: Int,
    override val reloadTime: Tick,
    override val reloadManager: ReloadManager
) : Reload {
    override fun reload(player: Player, gun: PlayerGun) {
        if (!check(gun.stats)) {
            return
        }
        gun.stats.reloading = true
        player.playSound(player.location, Sound.BLOCK_WOODEN_DOOR_OPEN, 1f,2f)
        reloadManager.register(gun, player, System.currentTimeMillis() + reloadTime.getMilliseconds())
    }
}