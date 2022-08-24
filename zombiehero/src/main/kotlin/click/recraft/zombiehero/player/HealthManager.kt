package click.recraft.zombiehero.player

import click.recraft.zombiehero.ZombieHero
import click.recraft.zombiehero.task.OneTickTimerTask
import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import java.util.*

object HealthManager: OneTickTimerTask {
    private val healths = hashMapOf<UUID, Int>()
    override fun loopEveryOneTick() {
        Bukkit.getOnlinePlayers().forEach {player ->
            sendPlayerHealth(player)
        }
    }
    private fun sendPlayerHealth(player: Player) {
        val damage = player.getPluginHealth()
        // 挿入されたデータが直前の場合と,healthが挿入されてから4秒立ったあとにデータを送るようにする
        // データを送ってから､またすぐにデータを送るとactionBarが正しい数値を表示しない
        val textComponent = TextComponent()
        textComponent.text = "${ChatColor.RED}♥${damage}"
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, textComponent)
    }

    fun Player.damagePluginHealth(damage: Int) {
        val hp = getPluginHealth()
        var currentHp = hp - damage
        if (currentHp < 0) {
            teleport(Bukkit.getWorld("world")!!.spawnLocation)
            currentHp = 1000
        }
        healths[uniqueId] = currentHp
        sendPlayerHealth(this)
    }

    fun Player.getPluginHealth(): Int {
        if (!healths.contains(uniqueId)) {
            healths[uniqueId] = 1000
        }
        return healths[uniqueId]!!
    }
}