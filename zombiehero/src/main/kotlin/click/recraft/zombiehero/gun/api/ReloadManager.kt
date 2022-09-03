package click.recraft.zombiehero.gun.api

import click.recraft.zombiehero.item.gun.Gun
import org.bukkit.entity.Player
import java.util.*

abstract class ReloadManager {
    internal val save: HashMap<UUID, Data> = hashMapOf()
    internal data class Data(
        val gun: Gun,
        var tick: Int,
        val reloadTime: Int,
        val uuid: UUID
    )
    fun register(gun: Gun, player: Player) {
        save[gun.unique] = Data(
            gun,
            gun.getReloadTime(),
            gun.getReloadTime(),
            player.uniqueId
        )
    }
    fun contains(gun: Gun): Boolean {
        return save.contains(gun.unique)
    }
    fun remove(gun: Gun) {
        save.remove(gun.unique)
    }
}