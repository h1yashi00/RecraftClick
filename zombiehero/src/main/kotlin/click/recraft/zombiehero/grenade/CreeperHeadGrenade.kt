package click.recraft.zombiehero.grenade

import click.recraft.zombiehero.Util
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.player.PlayerInteractEvent

//class CreeperHeadGrenade: Grenade(
//    Material.CREEPER_HEAD,
//    42342,
//    "CreeperHead",
//    20*3,
//    { loc ->
//        val entities = loc.world!!.getNearbyEntities(Util.makeBoundingBox(loc, 5.0))
//        loc.world!!.playSound(loc, Sound.ENTITY_GENERIC_EXPLODE, 0.5f,0.5f)
//        entities.forEach {
//            val vec = it.location.apply{y+=1}.toVector().subtract(loc.toVector())
//            if (vec.x == 0.0 && vec.y == 0.0 && vec.z == 0.0) return@forEach
//            it.velocity = vec.normalize().multiply(1.8)
//        }
//    }
//) {
//}
class GrenadeEvents: Listener {
    @EventHandler
    fun place(event: BlockPlaceEvent) {
        val player = event.player
        val holdingItem = player.inventory.itemInMainHand
        val loc = player.eyeLocation.clone()
//        ZombieHero.plugin.grenades.forEach { grenadeBase ->
//            val grenade = grenadeBase.isItem(holdingItem) ?: return@forEach
//            event.isCancelled = true
//            grenade.action(loc)
//        }
    }
    @EventHandler
    fun rightClick(event: PlayerInteractEvent) {
        if (!(event.action == Action.RIGHT_CLICK_BLOCK || event.action == Action.RIGHT_CLICK_AIR)) return
        val player = event.player
        val holdingItem = player.inventory.itemInMainHand
        val loc = player.eyeLocation.clone()
//        ZombieHero.plugin.grenades.forEach { grenadeBase ->
//            val grenade = grenadeBase.isItem(holdingItem) ?: return@forEach
//            grenade.action(loc)
//        }
    }
}