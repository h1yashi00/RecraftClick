package click.recraft.zombiehero.gun.api

import click.recraft.zombiehero.Util
import click.recraft.zombiehero.ZombieHero
import click.recraft.zombiehero.gun.base.Tick
import click.recraft.zombiehero.item.PlayerGun
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.entity.Player

interface Reload {
    val armo: Int
    // プレイヤーチェックするのが1sec/20tick の 1tick毎なので､ 50msでチェック
    val reloadTime: Tick
    fun check(gunStats: PlayerGun.GunStats): Boolean {
        if (gunStats.reloading) {
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

    fun reload(player: Player, gunStats: PlayerGun.GunStats)
}