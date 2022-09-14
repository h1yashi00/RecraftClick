package click.recraft.zombiehero.gun.api

import click.recraft.zombiehero.item.gun.Gun
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
        if (gun.stats.currentArmo == 0) {
            if (reloadManager is ReloadManagerOneBullet) {
                reloadManager.delayRegister(gun, player)
            }
            else {
                reloadManager.register(gun, player)
            }
        }
        else {
            reloadManager.register(gun, player)
        }
        val gunSound= gun.reloadSound
        player.playSound(player.location, gunSound.type.sound, gunSound.volume,gunSound.pitch)
    }
}