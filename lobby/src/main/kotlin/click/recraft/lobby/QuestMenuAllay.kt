package click.recraft.lobby

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractAtEntityEvent

class QuestMenuAllay: Listener {
    private val world = Bukkit.getWorld("world")!!
    private val allay = world.spawnEntity(world.spawnLocation.apply{chunk.load()}, EntityType.ALLAY).apply {
        if (this is LivingEntity) {
            setAI(false)
            isInvulnerable = true
        }
        ticksLived  = 999999999
        isSilent = true
        isCustomNameVisible = true
        customName = "${ChatColor.GREEN}クエスト"
    }
    @EventHandler
    fun playerInteractAllay(event: PlayerInteractAtEntityEvent) {
        if (event.rightClicked != allay) return
        val player = event.player
        player.openInventory(QuestMenu.menu.createInv(player))
    }
}