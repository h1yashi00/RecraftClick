package click.recraft.zombiehero.item.melee

import click.recraft.share.item
import click.recraft.share.protocol.TextureItem
import click.recraft.zombiehero.Util
import click.recraft.zombiehero.ZombieHero
import click.recraft.zombiehero.event.MeleeAttackPlayerEvent
import click.recraft.zombiehero.gun.api.GameSound
import click.recraft.zombiehero.gun.api.Tick
import click.recraft.zombiehero.item.CustomItem
import click.recraft.zombiehero.player.HealthManager.damagePluginHealth
import click.recraft.zombiehero.player.MeleeManager
import click.recraft.zombiehero.player.WalkSpeed
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerItemHeldEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.util.Vector
import java.util.*

class Melee (
    val name: String,
    private val damage: Int,
    private val attackSpan: Tick,
    private val knockBack: Double,
    textureItem: TextureItem,
    override val walkSpeed: Int,
    private val swingSound: GameSound
): WalkSpeed, CustomItem (
    item(
        textureItem.material,
        customModelData = textureItem.customModelData,
        localizedName = UUID.randomUUID().toString(), )
) {

    private fun calcAttack(
        source: Player,
        baseLoc: Location,
        range: Int,
        dir: Vector,
        particle: Particle
    ) {
        val loc = baseLoc.clone()
        val currentDir = dir.clone().apply {x /= 2; y /= 2; z /= 2}
        (0..(range * 2)).forEach { _ ->
            loc.apply { add(currentDir) }
            if (loc.block.type.isSolid) {
                return
            }
//            world.spawnParticle(particle, loc, 1)
            val box = Util.makeBoundingBox(loc, 0.1)
            val entities = loc.world!!.getNearbyEntities(box)
            entities.removeIf{ it == source }
            if (entities.isEmpty()) return@forEach
            entities.forEach { entity ->
                if (entity !is LivingEntity) return@forEach
                val customEvent = MeleeAttackPlayerEvent(
                    source = source,
                    melee = this,
                    damage = damage.toDouble(),
                    Util.isHeadLocation(box, entity.eyeLocation),
                    entity
                )
                Bukkit.getPluginManager().callEvent(customEvent)
                if (customEvent.isCancelled) {
                    return@forEach
                }
                customEvent.victim.damagePluginHealth(customEvent.source, customEvent.damage.toInt(), customEvent.melee, customEvent.isHeadShot)
                entity.velocity = dir.clone().multiply(knockBack).apply {y = 0.1}
            }
            return
        }
    }
    private fun attack(player: Player) {
        player.addPotionEffect(PotionEffect(PotionEffectType.SLOW_DIGGING, 20,10, false, false))
        player.swingMainHand()
        player.playSound(player.location, swingSound.type.sound, swingSound.volume, swingSound.pitch)
        calcAttack(player, player.eyeLocation, 3, player.eyeLocation.direction, Particle.DRIP_LAVA)
    }

    var holdingPlayer: UUID = UUID.randomUUID()
    var itemChanged = false
    var attackingStart = ZombieHero.plugin.getTime()
    var progress = 1F
    override fun currentItem(event: PlayerItemHeldEvent) {
        itemChanged = true
    }

    override fun prevItem(event: PlayerItemHeldEvent) {
        itemChanged = true
    }

    override fun inInvItemClick(clickType: ClickType, player: Player) {
    }

    // rightClickが3回ほど発生するので､それを阻止する
    override fun rightClick(event: PlayerInteractEvent) {
        if (MeleeManager.isAttacking(this)) {
            return
        }
        holdingPlayer = event.player.uniqueId
        progress = 1f
        attackingStart = ZombieHero.plugin.getTime()
        itemChanged = false
        MeleeManager.registerAttacking(this)
    }
    fun oneTick() {
        if (itemChanged) {
            MeleeManager.remove(this)
        }
        val player = Bukkit.getPlayer(holdingPlayer) ?: return
        val passed = ZombieHero.plugin.getTime() - attackingStart
        val progress = 1f - (passed.toFloat() / attackSpan.tick)
        player.sendExperienceChange(progress,0)
        if (progress <= 0) {
            attack(player)
            MeleeManager.remove(this)
        }
    }

    override fun leftClick(event: PlayerInteractEvent) {
    }

}