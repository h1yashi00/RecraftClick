package click.recraft.zombiehero.player

import click.recraft.zombiehero.Util
import click.recraft.zombiehero.ZombieHero
import click.recraft.zombiehero.item.CustomItem
import click.recraft.zombiehero.item.grenade.Grenade
import click.recraft.zombiehero.item.gun.Gun
import click.recraft.zombiehero.item.melee.Sword
import click.recraft.zombiehero.monster.api.MonsterManager
import click.recraft.zombiehero.task.OneTickTimerTask
import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.GameMode
import org.bukkit.Sound
import org.bukkit.entity.Player
import java.util.*


object HealthManager: OneTickTimerTask {
    private val healths = hashMapOf<UUID, Int>()
    override fun loopEveryOneTick() {
        Bukkit.getOnlinePlayers().forEach {player ->
            sendPlayerHealth(player)
        }
    }
    private fun createMessage(customItem: CustomItem?): String {
        customItem ?: return ""
        return when (customItem) {
            is Gun -> {
                val gunStats = customItem.stats
                "${gunStats.currentArmo}/${gunStats.maxArmo}"
            }
            is Sword -> {
                customItem.name
            }
            is Grenade -> {
                "${customItem.name}: ${customItem.getRemainingTime()}"
            }
            else -> {"not impl"}
        }
    }

    private fun sendPlayerHealth(player: Player) {
        val damage = player.getPluginHealth()
        // 挿入されたデータが直前の場合と,healthが挿入されてから4秒立ったあとにデータを送るようにする
        // データを送ってから､またすぐにデータを送るとactionBarが正しい数値を表示しない
        val item = player.inventory.itemInMainHand
        val customItem = ZombieHero.plugin.customItemFactory.getItem(item)
        val msg = createMessage(customItem)
        val textComponent = TextComponent()
        textComponent.text = "${ChatColor.RED}❤${damage} ${ChatColor.WHITE}${ChatColor.BOLD}${msg} ${ChatColor.BLUE}⚡${player.walkSpeed}"
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, textComponent)
    }



    fun Player.damagePluginHealth(damage: Int) {
        val hp = getPluginHealth()
        var currentHp = hp - damage
        if (currentHp < 0) {
            val monster = MonsterManager.get(this)
            currentHp = monster?.maxHealth ?: 1000
            world.playSound(location, monster?.deathSound ?: Sound.ENTITY_PLAYER_ATTACK_WEAK, 2f,0.1f)
            gameMode = GameMode.SPECTATOR
            val task = Util.createTask {
                gameMode = GameMode.SURVIVAL
                Bukkit.getOnlinePlayers().forEach { it.playSound(it, Sound.ENTITY_WITHER_SPAWN, 1f,0.5f) }
                teleport(ZombieHero.plugin.gameManager.world.randomSpawn())
            }
            Bukkit.getScheduler().runTaskLater(ZombieHero.plugin, task, 20 * 10)
        }
        healths[uniqueId] = currentHp
        sendPlayerHealth(this)
    }

    fun Player.healPluginHealth(heal: Int) {
        val hp = getPluginHealth()
        val currentHp = hp + heal
        healths[uniqueId] = currentHp
        sendPlayerHealth(this)
    }

    fun Player.getPluginHealth(): Int {
        if (!healths.contains(uniqueId)) {
            healths[uniqueId] = 1000
        }
        return healths[uniqueId]!!
    }

    fun clear() {
        healths.clear()
    }
}