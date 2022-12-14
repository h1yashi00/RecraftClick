package click.recraft.zombiehero.monster.api

import click.recraft.share.item
import click.recraft.zombiehero.player.HealthManager.setPluginHealth
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import java.util.*

abstract class Monster(
    val damageSound: Sound,
    val deathSound: Sound,
    val playerUUID: UUID,
    var walkSpeed: Int,
    val type: MonsterType,

    val maxHealth: Int,
    val chestPlate: ItemStack,
) {
    abstract val skill1: Skill
    abstract val skill2: Skill
    val head = type.head
    var isDead = false
//    private fun getDefaultItem(): ItemStack {
//        val factory = ZombieHero.plugin.customItemFactory
//        return factory.createZombieItem(CustomItemFactory.ZombieItem.ZombieGrenadeTouch).createItemStack()
//    }
    val uniqueId: UUID = UUID.randomUUID()

    fun initialize(player: Player) {
        val inv = player.inventory
        inv.clear()
        inv.helmet = head
        inv.chestplate = chestPlate
//        inv.addItem(getDefaultItem())
        inv.addItem(item(Material.GLASS,1))
        inv.setItem(skill1.index, skill1.item)
        inv.setItem(skill2.index, skill2.item)
        player.setPluginHealth(maxHealth)
    }
    fun passOneSec() {
        skill1.passOneSec()
        skill2.passOneSec()
    }

    abstract fun rightClick(player: PlayerInteractEvent)
}