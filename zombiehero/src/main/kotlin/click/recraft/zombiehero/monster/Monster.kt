package click.recraft.zombiehero.monster

import click.recraft.zombiehero.ZombieHero
import click.recraft.zombiehero.item.CustomItemFactory
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import java.util.*

interface Monster {
    fun getDefaultItem(): ItemStack {
        val factory = ZombieHero.plugin.customItemFactory
        return factory.createGrenade(CustomItemFactory.GrenadeType.TouchGrenade).createItemStack()
    }
    val uniqueId: UUID
    val playerUUID: UUID
    var walkSpeed: Float

    val skill1: Skill
    val skill2: Skill

    val health: Int
    val head: ItemStack
    val chestPlate: ItemStack

    fun initialize(player: Player) {
        val inv = player.inventory
        inv.clear()
        inv.helmet = head
        inv.chestplate = chestPlate
        inv.addItem(getDefaultItem())
        inv.setItem(skill1.index, skill1.item)
        inv.setItem(skill2.index, skill2.item)
    }
    fun passOneSec() {
        skill1.passOneSec()
        skill2.passOneSec()
    }

    fun rightClick(player: PlayerInteractEvent)
}