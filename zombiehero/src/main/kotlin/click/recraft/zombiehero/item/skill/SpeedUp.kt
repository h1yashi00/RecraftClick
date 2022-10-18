package click.recraft.zombiehero.item.skill

import click.recraft.share.extension.runTaskLater
import click.recraft.zombiehero.player.PlayerData.removeSkillSpeed
import click.recraft.zombiehero.player.PlayerData.setSkillSpeed
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class SpeedUp: SkillItem (
    Material.SUGAR,
    displayName = "${ChatColor.WHITE}SpeedUp",
    description = arrayListOf(
        "${ChatColor.WHITE}数秒間足が早くなる｡その反動で一定時間足が遅くなり､ジャンプができなくなる",
    )
) {
    override fun inInvItemClick(clickType: ClickType, player: Player) {
    }
    private val speedUp = 100
    private val speedDown = -100
    // 10 秒間足が早くなる
    private val speedUpEffectiveTime = (20 * 10)

    override fun rightClick(event: PlayerInteractEvent) {
        event.isCancelled = true
        val player = event.player
        player.world.playSound(player.location, Sound.ENTITY_WITHER_SHOOT, 2f, 2f)
        player.inventory.removeItem(createItemStack())
        player.setSkillSpeed(speedUp)
        // 走ったとの息切れ
        runTaskLater(speedUpEffectiveTime) {
            player.passengers.clear()
            player.setSkillSpeed(speedDown)
            val effect = PotionEffect(PotionEffectType.JUMP, 20 * 8, 238, false, false, false)
            player.addPotionEffect(effect)
        }

        runTaskLater(speedUpEffectiveTime + speedUpEffectiveTime) {
            player.removePotionEffect(PotionEffectType.JUMP)
            player.removeSkillSpeed()
        }
    }

    override fun leftClick(event: PlayerInteractEvent) {
        event.isCancelled = true
    }
}