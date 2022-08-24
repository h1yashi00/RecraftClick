package click.recraft.zombiehero.grenade

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.Sound
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitTask

interface GrenadeBase {
    val name: String
    val explosionFunction: (Location) -> Unit
    val explosionDelay: Int
//    fun isItem(itemStack: ItemStack?): GrenadeBase? {
//    }
//
//    fun getItem(): ItemStack {
//        return getItem("${ChatColor.YELLOW}$name")
//    }
//
//    fun action(location: Location) {
//        val loc = location.clone()
//        val item = loc.world!!.dropItem(loc, getItem()).apply {
//            pickupDelay = 100000
//            velocity = loc.direction.multiply(1)
//        }
//        var count = 5
//        val soundTask: (BukkitTask) -> Unit = {
//            if (count == 0) it.cancel()
//            loc.world!!.playSound(loc, Sound.ENTITY_TNT_PRIMED, 1F,1F)
//            count -= 1
//        }
//        Bukkit.getScheduler().runTaskTimer(ZombieHero.plugin, soundTask, 0,10)
//
//        val task: (BukkitTask) -> Unit = {
//            explosionFunction.invoke(item.location)
//            item.remove()
//        }
//        Bukkit.getScheduler().runTaskLater(ZombieHero.plugin, task, explosionDelay.toLong())
//    }
}