package click.recraft.zombiehero.item.skill.grenade

import click.recraft.zombiehero.Util
import click.recraft.zombiehero.gun.api.GameSound
import click.recraft.zombiehero.gun.api.Tick
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.Sound
import org.bukkit.entity.Item
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerItemHeldEvent

class ZombieBomb: Grenade(
    "ZombieBomb",
    Tick.sec(1.5),
    200,
    Tick.sec(30.0),
    999999,
    0,
    arrayListOf(
        "${ChatColor.WHITE}一定時間で爆発し､ふっとばすことができる"
    )
) {
    override fun pickUp(player: Player, item: Item) {
    }

    override fun currentItem(event: PlayerItemHeldEvent) {
        val loc = event.player.location
        val sound = GameSound(GameSound.Type.HORROR_MONSTER_SCREAM, volume = 0.1F)
        loc.world!!.playSound(loc, sound.type.sound, sound.volume, sound.pitch)
    }

    override fun explosion(entity: Item, location: Location) {
        val entities = location.world!!.getNearbyEntities(Util.makeBoundingBox(location, 5.0))
        location.world!!.playSound(location, Sound.ENTITY_GENERIC_EXPLODE, 0.2f,0.5f)
        entities.forEach {
            val vec = it.location.apply { y += 1 }.toVector().subtract(location.toVector())
            if (vec.x == 0.0 && vec.y == 0.0 && vec.z == 0.0) return@forEach
            it.velocity = vec.normalize().multiply(1.8)
        }
    }
}