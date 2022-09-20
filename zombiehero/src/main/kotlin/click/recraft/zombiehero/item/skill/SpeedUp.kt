package click.recraft.zombiehero.item.skill

import click.recraft.zombiehero.Util
import click.recraft.zombiehero.ZombieHero
import click.recraft.zombiehero.player.PlayerData.removeSkillSpeed
import click.recraft.zombiehero.player.PlayerData.setSkillSpeed
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.player.PlayerInteractEvent

class SpeedUp: SkillItem (
    Material.SUGAR,
) {
    override fun inInvItemClick(clickType: ClickType, player: Player) {
    }
    private val speedUp = 200
    private val speedDown = -100
    // 10 秒間足が早くなる
    private val speedUpEffectiveTime = (20 * 10).toLong()

    override fun rightClick(event: PlayerInteractEvent) {
        event.isCancelled = true
        val player = event.player
        player.inventory.removeItem(createItemStack())
        player.setSkillSpeed(speedUp)
        createPassenger(player)
        // 走ったとの息切れ
        val shotBreath = Util.createTask {
            removePassenger(player)
            player.passengers.clear()
            player.setSkillSpeed(speedDown)
        }
        Bukkit.getScheduler().runTaskLater(ZombieHero.plugin, shotBreath, speedUpEffectiveTime)
        val returnNormal = Util.createTask {
            player.removeSkillSpeed()
        }
        Bukkit.getScheduler().runTaskLater(ZombieHero.plugin, returnNormal, speedUpEffectiveTime + 20 * 3)
    }

    override fun leftClick(event: PlayerInteractEvent) {
        event.isCancelled = true
    }
}