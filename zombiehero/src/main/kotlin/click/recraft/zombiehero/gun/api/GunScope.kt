package click.recraft.zombiehero.gun.api

import click.recraft.zombiehero.item.gun.Gun
import click.recraft.zombiehero.item.gun.ShootManager
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

open class GunScope(
    material: Material,
    name: String,
    customModeValue: Int,
    private val shootManager: ShootManager,
    private val reload: Reload,
    private val shot: Shot,
    override var walkSpeed: Int,
): Gun(
    material,
    name,
    customModeValue,
    shootManager,
    reload,
    shot,
    walkSpeed
) {
    private var scope = false

    private var qDropCheck = System.currentTimeMillis()
    // qドロップする際に､InteractEventが発生するようになってしまったので､
    // それを回避するためのもの｡ interact と dropイベントが全く同じタイミングで発生した場合は､回避する
    fun isQDrop(): Boolean {
        if (qDropCheck == System.currentTimeMillis()) {
            return true
        }
        qDropCheck = System.currentTimeMillis()
        return false
    }

    // drop method
    fun scope(player: Player) {
        if (isQDrop()) {
            return
        }
        if (scope) {
            player.inventory.helmet = ItemStack(Material.AIR)
            walkSpeed += 30
            scope = false
        }
        else {
            player.inventory.helmet = ItemStack(Material.CARVED_PUMPKIN)
            walkSpeed += -30
            scope = true
        }
    }
}