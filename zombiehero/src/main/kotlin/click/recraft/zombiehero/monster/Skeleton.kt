package click.recraft.zombiehero.monster

import click.recraft.zombiehero.Util
import click.recraft.zombiehero.ZombieHero
import org.bukkit.Bukkit
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.enchantments.Enchantment
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.LeatherArmorMeta
import org.bukkit.inventory.meta.PotionMeta
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import java.util.*

class Skeleton(override val playerUUID: UUID) : Monster {
    override val damageSound: Sound = Sound.ENTITY_SKELETON_HURT
    override val uniqueId: UUID = UUID.randomUUID()
    override var walkSpeed: Int = 0
    private class Skill1(
        override val monster: Monster
    ): Skill {
        override val item: ItemStack = ItemStack(Material.BOW).apply {
            val meta  = itemMeta!!
            meta.addEnchant(Enchantment.ARROW_INFINITE, 1, true)
            meta.isUnbreakable = true
            itemMeta = meta
        }
        override val coolDownTime: Int = 30
        override val index: Int = 3
        override var coolDown: Int = 0
        override var active: Boolean = false

        override fun active() {
            val player = Bukkit.getPlayer(monster.playerUUID) ?: return
            player.inventory.setItem(30, ItemStack(Material.ARROW))
            val task = Util.createTask {
                player.inventory.remove(ItemStack(Material.ARROW))
            }
            Bukkit.getScheduler().runTaskLater(ZombieHero.plugin, task, 1)
        }
    }
    private class Skill2(override val monster: Monster): Skill {
        override val item: ItemStack = ItemStack(Material.POTION).apply {
            val meta = itemMeta!! as PotionMeta
            meta.color = Color.WHITE
            meta.addCustomEffect(PotionEffect(PotionEffectType.INVISIBILITY, 20 * 30, 0), true)
            itemMeta = meta
        }
        override val coolDownTime: Int = 30
        override val index: Int = 4
        override var coolDown: Int = 0
        override var active: Boolean = false

        override fun active() {
            val player = Bukkit.getPlayer(monster.playerUUID) ?: return
            val potionEffect = (item.itemMeta as PotionMeta).customEffects
            potionEffect.forEach {
                player.addPotionEffect(it)
            }
            player.inventory.helmet = ItemStack(Material.AIR)
            player.inventory.chestplate = ItemStack(Material.AIR)
            active = true
            val task = Util.createTask {
                player.inventory.helmet     = monster.head
                player.inventory.chestplate = monster.chestPlate
                active = false
            }
            Bukkit.getScheduler().runTaskLater(ZombieHero.plugin, task, 20 * 15)
            coolDown = coolDownTime
        }
    }
    override val skill1: Skill = Skill1(this)
    override val skill2: Skill = Skill2(this)
    override val health: Int = 3000
    override val head: ItemStack = ItemStack(Material.SKELETON_SKULL)
    override val chestPlate: ItemStack = ItemStack(Material.LEATHER_CHESTPLATE).apply {
        val meta = itemMeta!!
        meta as LeatherArmorMeta
        meta.setColor(Color.WHITE)
        itemMeta = meta
    }

    override fun rightClick(event: PlayerInteractEvent) {
        val player = event.player
        val monster = ZombieHero.plugin.monsterManager.get(player) ?: return
        if (monster !is Skeleton) return
        val item = player.inventory.itemInMainHand
        if (skill1.isSame(item)) {
            skill1.active()
        }
        if (skill2.isSame(item)) {
            event.isCancelled = true
            skill2.active()
        }
    }
}