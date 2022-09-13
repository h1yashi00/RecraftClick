package click.recraft.zombiehero.item.melee

import click.recraft.share.item
import click.recraft.zombiehero.Util
import click.recraft.zombiehero.ZombieHero
import click.recraft.zombiehero.gun.api.GameSound
import click.recraft.zombiehero.gun.api.Tick
import click.recraft.zombiehero.item.CustomItem
import click.recraft.zombiehero.player.WalkSpeed
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerItemHeldEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import java.util.*

class Sword (
    val name: String,
    private val meleeCoolDownManager: MeleeCoolDownManager,
    private val damage: Int,
    val coolDown: Tick,
    customModelData: Int,
    override val walkSpeed: Int,
    val swingSound: GameSound
): WalkSpeed, CustomItem (
    item(
        Material.PINK_DYE,
        customModelData = customModelData,
        localizedName = UUID.randomUUID().toString(),
    )
) {

    fun attack(player: Player) {
        player.addPotionEffect(PotionEffect(PotionEffectType.SLOW_DIGGING, 20,10))
        player.swingMainHand()
        player.playSound(player.location, swingSound.type.sound, swingSound.volume, swingSound.pitch)

        val loc = player.eyeLocation.clone()
        var count = 3
        val task = Util.createTask {
            if (count <= 0) {
                cancel()
                return@createTask
            }
            val dir = loc.direction
            loc.apply {
                add(dir.multiply(1))
            }
            val box = Util.makeBoundingBox(loc, 1.0)
            val entities = loc.world!!.getNearbyEntities(box)
            loc.world!!.spawnParticle(Particle.SWEEP_ATTACK, loc, 1)
            count += -1
            entities.forEach {entity ->
                if (entity !is LivingEntity) return@forEach
                if (entity == player) return@forEach
                entity.damage(damage.toDouble())
                entity.noDamageTicks = 0
                entity.velocity = player.location.direction.clone().multiply(1)
            }
        }
        Bukkit.getScheduler().runTaskTimer(ZombieHero.plugin, task,0,2)
    }

    override fun currentItem(event: PlayerItemHeldEvent) {
    }

    override fun prevItem(event: PlayerItemHeldEvent) {
    }

    override fun inInvItemClick(clickType: ClickType, player: Player) {
    }

    override fun rightClick(event: PlayerInteractEvent) {
        val player = event.player
        val item = player.inventory.itemInMainHand
        event.isCancelled = true
        if (meleeCoolDownManager.contains(this.unique)) {
            return
        }
        meleeCoolDownManager.register(this, player, item)
    }

    override fun leftClick(event: PlayerInteractEvent) {
    }
}