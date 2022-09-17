package click.recraft.zombiehero.monster

import click.recraft.zombiehero.Util
import click.recraft.zombiehero.ZombieHero
import click.recraft.zombiehero.monster.api.Monster
import click.recraft.zombiehero.monster.api.MonsterManager
import click.recraft.zombiehero.monster.api.MonsterType
import click.recraft.zombiehero.monster.api.Skill
import org.bukkit.Bukkit
import org.bukkit.ChatColor
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

class Skeleton(playerUUID: UUID) : Monster(
    Sound.ENTITY_SKELETON_HURT,
    Sound.ENTITY_SKELETON_DEATH,
    walkSpeed = 0,
    playerUUID = playerUUID,
    maxHealth = 3000,
    head = ItemStack(Material.SKELETON_SKULL),
    chestPlate = ItemStack(Material.LEATHER_CHESTPLATE).apply {
        val meta = itemMeta!!
        meta as LeatherArmorMeta
        meta.setColor(Color.WHITE)
        itemMeta = meta
    }
) {
    override val skill1: Skill = Skill1(this)
    override val skill2: Skill = Skill2(this)
    override val type: MonsterType = MonsterType.SKELETON

    private class Skill1(
        override val monster: Monster
    ): Skill {
        override val item: ItemStack = ItemStack(Material.BOW).apply {
            val meta  = itemMeta!!
            meta.addEnchant(Enchantment.ARROW_INFINITE, 1, true)
            meta.lore = arrayListOf(
                "${ChatColor.WHITE}${ChatColor.BOLD}スケルトン弓",
                "${ChatColor.WHITE}${ChatColor.BOLD}相手を引き寄せる",
                "${ChatColor.WHITE}${ChatColor.BOLD}回復する",
                "${ChatColor.WHITE}${ChatColor.BOLD}足が速くなる",
            )
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
            meta.addCustomEffect(PotionEffect(PotionEffectType.GLOWING, 20*5, 0), true)
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
            Bukkit.getOnlinePlayers().forEach {
                it.playSound(it, Sound.ENTITY_SKELETON_AMBIENT, 1F, 0.1F)
            }
            active = true
            val task = Util.createTask {
                player.inventory.helmet     = monster.head
                player.inventory.chestplate = monster.chestPlate
                player.removePotionEffect(PotionEffectType.INVISIBILITY)
                active = false
            }
            Bukkit.getScheduler().runTaskLater(ZombieHero.plugin, task, 20 * 15)
            coolDown = coolDownTime
        }
    }

    override fun rightClick(event: PlayerInteractEvent) {
        val player = event.player
        val monster = MonsterManager.get(player) ?: return
        if (monster !is Skeleton) return
        val item = player.inventory.itemInMainHand
        if (skill1.isSame(item)) {
            skill1.active()
        }
        if (skill2.isSame(item)) {
            event.isCancelled = true
            if (!skill2.coolDownCheck()) {
                return
            }
            skill2.active()
        }
    }
}