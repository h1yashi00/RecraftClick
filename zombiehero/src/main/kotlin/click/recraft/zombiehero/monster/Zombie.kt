package click.recraft.zombiehero.monster

import click.recraft.zombiehero.Util
import click.recraft.zombiehero.ZombieHero
import click.recraft.zombiehero.player.HealthManager.healPluginHealth
import org.bukkit.*
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.LeatherArmorMeta
import java.util.*


class Zombie(override val playerUUID: UUID) : Monster {
    private class Skill1(override val monster: Zombie) : Skill {
        override val item: ItemStack = ItemStack(Material.GOLDEN_APPLE)
        override val coolDownTime: Int = 100
        override val index: Int = 6
        override var coolDown: Int = 0
        override var active: Boolean = false
        override fun active() {
            if (!coolDownCheck()) {
                return
            }
            val player = Bukkit.getPlayer(monster.playerUUID) ?: return
            player.healPluginHealth(monster.health)
            player.world.playSound(player.location, Sound.ENTITY_PLAYER_BURP, 1f,2f)
            player.world.spawnParticle(Particle.HEART, player.location, 10, 1.5,1.5,1.5)
            coolDown = coolDownTime
        }
    }
    private class Skill2(override val monster: Monster) : Skill {
        override val item: ItemStack = ItemStack(Material.NETHERITE_CHESTPLATE)
        override val coolDownTime: Int = 100
        override val index: Int = 5
        override var coolDown: Int = 0
        override var active: Boolean = false
        override fun active() {
            if (!coolDownCheck()) {
                return
            }
            val player = Bukkit.getPlayer(monster.playerUUID) ?: return
            player.inventory.chestplate = (ItemStack(Material.NETHERITE_CHESTPLATE))
            active = true
            val task = Util.createTask {
                player.inventory.chestplate = monster.chestPlate
                active = false
            }
            Bukkit.getScheduler().runTaskLater(ZombieHero.plugin, task, 20)
            coolDown = coolDownTime
        }
    }

    override val damageSound: Sound = Sound.ENTITY_ZOMBIE_HURT
    override val uniqueId: UUID = UUID.randomUUID()
    override var walkSpeed: Int = 0
    override val skill1: Skill = Skill1(this)
    override val skill2: Skill = Skill2(this)

    override val health: Int = 3000
    override val head: ItemStack  = ItemStack(Material.ZOMBIE_HEAD)

    override val chestPlate: ItemStack = ItemStack(Material.LEATHER_CHESTPLATE).apply {
        val meta = itemMeta!!
        meta as LeatherArmorMeta
        meta.setColor(Color.GREEN)
        itemMeta = meta
    }


    override fun rightClick(event: PlayerInteractEvent) {
        val player = event.player
        val item = player.inventory.itemInMainHand
        if (skill1.isSame(item)) {
            event.isCancelled = true
            if (!skill1.coolDownCheck()) {
                return
            }
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