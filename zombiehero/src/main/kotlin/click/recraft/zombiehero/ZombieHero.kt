package click.recraft.zombiehero

import click.recraft.share.*
import click.recraft.zombiehero.grenade.CreeperHeadGrenade
import click.recraft.zombiehero.grenade.PopGrenade
import click.recraft.zombiehero.gun.AK47
import click.recraft.zombiehero.gun.Saiga
import click.recraft.zombiehero.gun.base.GunBase
import click.recraft.zombiehero.melee.Dagger
import click.recraft.zombiehero.melee.SwordBase
import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import org.bukkit.scheduler.BukkitTask
import java.util.UUID

class ZombieHero: KotlinPlugin(), Listener {
    companion object {
        lateinit var plugin: ZombieHero
    }

    val guns: MutableList<GunBase> by lazy {
        val gun = arrayListOf<GunBase>()
        gun.add(AK47())
        gun.add(Saiga())
        gun
    }
    val swords: MutableList<SwordBase> by lazy {
        val sword = arrayListOf<SwordBase>()
        sword.add(Dagger())
        sword
    }
    val grenades: MutableList<GrenadeBase> by lazy{
        val grenade = arrayListOf<GrenadeBase>()
        grenade.add(PopGrenade())
        grenade.add(CreeperHeadGrenade())
        grenade
    }
    private val walkingSpeedTask: (BukkitTask) -> Unit = {Bukkit.getOnlinePlayers().forEach {player ->
        val item = player.inventory.itemInMainHand
        var walkSpeed = 0.2F
        guns.forEach {
            val gun = it.isItem(item) ?: return@forEach
            walkSpeed = gun.walkingSpeed
        }
        swords.forEach {
            val sword = it.isItem(item) ?: return@forEach
            walkSpeed = sword.walkingSpeed
        }
        player.walkSpeed = walkSpeed
    }}
    override fun onEnable() {
        plugin = this
        // hpを送るscheduler
        // 1sec = 1000ms
        ShopMenu.load()
        // repeat tasks
        val healthTask : (BukkitTask) -> Unit = {Bukkit.getOnlinePlayers().forEach(ZombieHeroData.sendPlayerHealth)}
        Bukkit.getScheduler().runTaskTimer(this, healthTask, 0, 20)
        Bukkit.getScheduler().runTaskTimer(this, walkingSpeedTask, 0, 2)
        server.pluginManager.registerEvents(PlayerJoin(), this)
        server.pluginManager.registerEvents(PlayerQuit(), this)
        server.pluginManager.registerEvents(this, this)
        server.pluginManager.registerEvents(HoldKeyEvent(), this)
        server.pluginManager.registerEvents(PlayerDamage(), this)
        server.pluginManager.registerEvents(MonsterSpawn(), this)
        server.pluginManager.registerEvents(SwordEvents(), this)
        server.pluginManager.registerEvents(GrenadeEvents(), this)
//        server.pluginManager.registerEvents(LeftClickBlockExplosion(), this)
        super.onEnable()
    }

    override fun onDisable() {
    }
}

object ZombieHeroData {
    // データベースに登録しない情報
    private val health = hashMapOf<UUID, Int>()
    private fun Player.getPluginHealth(): Int {
        if (!ZombieHeroData.health.contains(uniqueId)) {
            ZombieHeroData.health[uniqueId] = 1000
        }
        return ZombieHeroData.health[uniqueId]!!
    }
    fun Player.damagePluginHealth(damage: Int) {
        val hp = getPluginHealth()
        var currentHp = hp - damage
        if (currentHp < 0) {
            teleport(Bukkit.getWorld("world")!!.spawnLocation)
            currentHp = 1000
        }
        ZombieHeroData.health[uniqueId] = currentHp
        sendPlayerHealth.invoke(this)
    }

    val sendPlayerHealth: (Player)-> Unit = {player ->
        val damage = player.getPluginHealth()
        // 挿入されたデータが直前の場合と,healthが挿入されてから4秒立ったあとにデータを送るようにする
        // データを送ってから､またすぐにデータを送るとactionBarが正しい数値を表示しない
        val textComponent = TextComponent()
        textComponent.text = "${ChatColor.RED}♥${damage}"
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, textComponent)
    }
}

