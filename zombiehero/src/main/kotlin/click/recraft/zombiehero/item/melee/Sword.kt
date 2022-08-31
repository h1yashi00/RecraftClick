package click.recraft.zombiehero.item.melee

import click.recraft.share.item
import click.recraft.zombiehero.Util
import click.recraft.zombiehero.ZombieHero
import click.recraft.zombiehero.gun.api.Tick
import click.recraft.zombiehero.item.CustomItem
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.block.Action
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot
import java.util.*

class Sword (
    private val meleeCoolDownManager: MeleeCoolDownManager,
    private val damage: Int,
    val coolDown: Tick
): CustomItem(
    item(
        Material.BLACK_DYE,
        customModelData = 3892,
        localizedName = UUID.randomUUID().toString(),
    )
) {

    fun attack(player: Player) {
        player.playSound(player.location, Sound.BLOCK_NOTE_BLOCK_HARP, 1f,1f)

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

    override fun inInvItemClick(clickType: ClickType, player: Player) {
    }

    override fun itemInteract(event: PlayerInteractEvent, equipmentSlot: EquipmentSlot) {
        val action = event.action
        if (action == Action.RIGHT_CLICK_BLOCK || action == Action.RIGHT_CLICK_AIR) {
            return
        }
        val player = event.player
        val item = player.inventory.itemInMainHand
        event.isCancelled = true
        if (meleeCoolDownManager.contains(this.unique)) {
            return
        }
        meleeCoolDownManager.register(this, player, item)
    }
}