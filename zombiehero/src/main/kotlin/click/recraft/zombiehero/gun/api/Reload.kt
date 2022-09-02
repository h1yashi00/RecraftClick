package click.recraft.zombiehero.gun.api

import click.recraft.zombiehero.item.gun.Gun
import org.bukkit.Sound
import org.bukkit.entity.Player

class Reload(
    val armo: Int,
    // プレイヤーチェックするのが1sec/20tick の 1tick毎なので､ 50msでチェック
    val reloadTime: Tick,
    val reloadManager: ReloadManager,
){
    fun check(gun: Gun): Boolean {
        val gunStats = gun.stats
        if (gun.isRelaoding()) {
            return false
        }
        if (gunStats.totalArmo <= 0) {
            return false
        }
        if (gunStats.currentArmo == armo) {
            return false
        }
        return true
    }

    fun reload(player: Player, gun: Gun) {
        if (!check(gun)) {
            return
        }
        reloadManager.register(gun, player)
        player.playSound(player.location, Sound.BLOCK_WOODEN_DOOR_OPEN, 1f,2f)
    }
}