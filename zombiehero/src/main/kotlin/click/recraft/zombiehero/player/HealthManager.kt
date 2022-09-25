package click.recraft.zombiehero.player

import click.recraft.zombiehero.UseNms
import click.recraft.zombiehero.ZombieHero
import click.recraft.zombiehero.event.PlayerDeadPluginHealthEvent
import click.recraft.zombiehero.event.PluginHealthDamageEvent
import click.recraft.zombiehero.item.CustomItem
import click.recraft.zombiehero.item.grenade.Grenade
import click.recraft.zombiehero.item.gun.Gun
import click.recraft.zombiehero.item.melee.Sword
import click.recraft.zombiehero.player.PlayerData.isPlayerSkillHeadShot
import click.recraft.zombiehero.task.OneTickTimerTask
import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.LivingEntity
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

    // this == damagerの場合は自害(Fall, 手榴弾?など)である
    fun LivingEntity.damagePluginHealth(
        damager: LivingEntity,
        damage: Int,
        customItem: CustomItem?,
        isHeadshot: Boolean = false
    ) {
        val headShotSkill = Bukkit.getPlayer(damager.uniqueId)?.isPlayerSkillHeadShot() ?: false
        val dmg = if (isHeadshot || headShotSkill) {damage * 2} else {damage}
        val pluginHealthDamageEvent = PluginHealthDamageEvent(
            damager ,
            this,
            dmg,
           customItem,
            isHeadshot
        )
        Bukkit.getPluginManager().callEvent(pluginHealthDamageEvent)
        if (pluginHealthDamageEvent.isCancelled) {
            return
        }
        // damage animationのために必要
        UseNms.sendDamageAnimationPacket(pluginHealthDamageEvent.victim)
         // human
        if (pluginHealthDamageEvent.victim !is Player) return
        val hp = pluginHealthDamageEvent.victim.getPluginHealth()
        var currentHp = hp - pluginHealthDamageEvent.damage
        if (currentHp <= 0) {
            val event = PlayerDeadPluginHealthEvent (
                pluginHealthDamageEvent.victim,
                pluginHealthDamageEvent.damager,
                pluginHealthDamageEvent.sourceCustomItem,
                pluginHealthDamageEvent.isHeadShot
            )
            Bukkit.getPluginManager().callEvent(event)
            currentHp = event.reviveHp
        }
        healths[uniqueId] = currentHp
        sendPlayerHealth(pluginHealthDamageEvent.victim)
    }

    fun Player.healPluginHealth(heal: Int) {
        val hp = getPluginHealth()
        val currentHp = hp + heal
        healths[uniqueId] = currentHp
        sendPlayerHealth(this)
    }

    // ゾンビになった際などのベースのHPが変更された際に使用する
    fun Player.setPluginHealth(health: Int) {
        healths[uniqueId] = health
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