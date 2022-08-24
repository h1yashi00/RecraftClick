package click.recraft.zombiehero

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitTask
import org.bukkit.util.BoundingBox
import java.util.*

//class SwordEvents: Listener {
//    init{
//        val task: (BukkitTask) -> Unit = {
//            Bukkit.getOnlinePlayers().forEach {player ->
//                var item = player.inventory.itemInMainHand
//                val itemSlot = player.inventory.heldItemSlot
//                val pairItem = pastItem[player.uniqueId]
//                val prevItem = pairItem?.first
//                val slot = pairItem?.second
//                if (prevItem != item) {
//                    ZombieHero.plugin.swords.forEach { val a = it.isItem(prevItem) ?: return@forEach; player.inventory.remove(prevItem!!); player.inventory.setItem(slot!!, a.getItem(a.swapAttackTime)) }
//                    pastItem[player.uniqueId] = Pair(item, itemSlot)
//                    holdingSwordPlayer.remove(player.uniqueId)
//                    return@forEach
//                }
//                ZombieHero.plugin.swords.forEach {swordBase ->
//                    val sword = swordBase.isItem(item) ?: return@forEach
//                    val tick = holdingSwordPlayer[player.uniqueId] ?: sword.swapAttackTime
//                    val passedTick = tick.minus(0.1)
//                    val showTick = if (passedTick < 0) { 0.0 } else {passedTick}
//                    holdingSwordPlayer[player.uniqueId] = showTick
//                    item = sword.getItem(showTick)
//                    player.inventory.setItemInMainHand(item)
//                }
//                pastItem[player.uniqueId] = Pair(item, itemSlot)
//            }
//        }
//        Bukkit.getScheduler().runTaskTimer(ZombieHero.plugin, task, 1,2)
//    }
//    private val holdingSwordPlayer = hashMapOf<UUID, Double>()
//    private val pastItem = hashMapOf<UUID, Pair<ItemStack, Int>>()
//
//    private fun makeBoundingBox(loc: Location): BoundingBox {
//        val hitbox = 0.1
//        val x1 = loc.x - hitbox
//        val y1 = loc.y - hitbox
//        val z1 = loc.z - hitbox
//
//        val x2 = loc.x + hitbox
//        val y2 = loc.y + hitbox
//        val z2 = loc.z + hitbox
//        return BoundingBox(x1, y1, z1, x2, y2, z2)
//    }
//
//    @EventHandler
//    fun playerAttack(event: EntityDamageByEntityEvent) {
//        val player = if (event.damager is Player) {
//            event.damager as Player
//        } else {
//            return
//        }
//        event.isCancelled = true
//        val loc = player.location.clone().apply { y += player.eyeHeight }
//        val dir = player.location.direction
//        val a = dir.multiply(1)
//        (0..2 * 2).forEach {
//            loc.apply { x += a.x /2 ; y += a.y /2; z += a.z/ 2 }
//            val boundingBox = makeBoundingBox(loc)
//            val entity = loc.world!!.getNearbyEntities(boundingBox)
//            if (entity.isNotEmpty()) {
//                val item = player.inventory.itemInMainHand
//                val victim = if (entity.first() is LivingEntity) {
//                    entity.first() as LivingEntity
//                } else return@forEach
//                if (entity.first() == player) return@forEach
//                ZombieHero.plugin.swords.forEach { swordBase ->
//                    val sword = swordBase.isItem(item) ?: return@forEach
//                    if (!sword.isReady(item)) {
//                        return
//                    }
//                    val dmg = if (Util.isHeadLocation(boundingBox, victim.eyeLocation)) {
//                        loc.world!!.spawnParticle(Particle.BLOCK_CRACK, loc, 10, Material.REDSTONE_BLOCK.createBlockData())
//                        sword.damage * 2
//                    } else {
//                        loc.world!!.spawnParticle(Particle.BLOCK_CRACK, loc, 10, Material.QUARTZ_BLOCK.createBlockData())
//                        sword.damage * 1
//                    }
//                    victim.damage(dmg.toDouble())
//                    victim.velocity = a
//                    val update = sword.getItem()
//                    player.inventory.setItemInMainHand(update)
//                }
//                return
//            }
//        }
//    }
//}