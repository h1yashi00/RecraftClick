package click.recraft.zombiehero

import click.recraft.zombiehero.item.grenade.Grenade
import click.recraft.zombiehero.gun.api.MultiShot
import click.recraft.zombiehero.gun.api.OneShot
import click.recraft.zombiehero.gun.api.ReloadFullBullet
import click.recraft.zombiehero.gun.api.ReloadOneBullet
import click.recraft.zombiehero.gun.api.Tick
import click.recraft.zombiehero.item.grenade.HitGrenade
import click.recraft.zombiehero.item.grenade.TouchGrenade
import click.recraft.zombiehero.item.gun.PlayerGun
import click.recraft.zombiehero.item.melee.Sword
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractAtEntityEvent
import org.bukkit.event.player.PlayerJoinEvent

class PlayerJoin: Listener {
    @EventHandler
    fun playerInteract(event: PlayerInteractAtEntityEvent) {
        if (event.rightClicked.type != EntityType.ARMOR_STAND) return
        event.isCancelled = true
    }
    @EventHandler
    fun joinPlayer(event: PlayerJoinEvent) {
        val player = event.player
        player.foodLevel = 20
        player.walkSpeed = 0.20F
        player.inventory.clear()
        val gun = PlayerGun (
            material = Material.STICK,
            name = "AK-47",
            customModeValue = 30,
            manager = ZombieHero.plugin.playerGunManager,
            shootManager = ZombieHero.plugin.shootManager,
            reload = ReloadOneBullet (
                7,
                Tick.sec(1.0),
                ZombieHero.plugin.reloadManager
            ),
            shot = MultiShot (
                Tick.sec(0.5),
                10,
                5,
                0.2,
                0.1,
                0.0F,
                0.0F,
                Tick.sec(0.3)
            ),
            walkSpeed = 0.25F
        ).apply {
            initialize()
        }
        val gun2 = PlayerGun (
            material = Material.PINK_DYE,
            name = "WIWIWI",
            customModeValue = 30,
            manager = ZombieHero.plugin.playerGunManager,
            shootManager = ZombieHero.plugin.shootManager,
            reload = ReloadFullBullet (
                30,
                Tick.sec(1.3),
                ZombieHero.plugin.reloadManager
            ),
            shot = OneShot(
                Tick.sec(0.1),
                10,
                0.3,
                0.01,
                0.0F,
                0.0F,
                Tick.sec(0.05)
            ),
            walkSpeed = 0.25F
        ).apply { initialize() }
        val gun3 = PlayerGun (
            material = Material.PINK_DYE,
            name = "WIWIWI",
            customModeValue = 30,
            manager = ZombieHero.plugin.playerGunManager,
            shootManager = ZombieHero.plugin.shootManager,
            reload = ReloadFullBullet (
                7,
                Tick.sec(1.3),
                ZombieHero.plugin.reloadManager
            ),
            shot = OneShot(
                Tick.sec(0.7),
                70,
                1.0,
                0.01,
                0.0F,
                0.0F,
                Tick.sec(0.5)
            ),
            walkSpeed = 0.25F
        ).apply {initialize()}
        val grenade = Grenade(ZombieHero.plugin.grenadeManager, "creeper", Tick.sec(1.5)) {loc ->
            val entities = loc.world!!.getNearbyEntities(Util.makeBoundingBox(loc, 5.0))
            loc.world!!.playSound(loc, Sound.ENTITY_GENERIC_EXPLODE, 0.5f,0.5f)
            entities.forEach {
                val vec = it.location.apply { y += 1 }.toVector().subtract(loc.toVector())
                if (vec.x == 0.0 && vec.y == 0.0 && vec.z == 0.0) return@forEach
                it.velocity = vec.normalize().multiply(1.8)
            }
        }.apply { initialize() }
        val touchGrenade = TouchGrenade(ZombieHero.plugin.touchGrenadeManager, "touchCreeper", Tick.sec(3.0)) { loc ->
            val entities = loc.world!!.getNearbyEntities(Util.makeBoundingBox(loc, 5.0))
            loc.world!!.playSound(loc, Sound.ENTITY_GENERIC_EXPLODE, 0.5f,0.5f)
            entities.forEach {
                val vec = it.location.apply { y += 1 }.toVector().subtract(loc.toVector())
                if (vec.x == 0.0 && vec.y == 0.0 && vec.z == 0.0) return@forEach
                it.velocity = vec.normalize().multiply(1.8)
            }
        }.apply { initialize() }
        val melee = Sword(
            ZombieHero.plugin.meleeManager,
            ZombieHero.plugin.meleeCoolDownManager,
            100,
            Tick.sec(1.0),
        ).apply {initialize()}
        val melee2 = Sword(
            ZombieHero.plugin.meleeManager,
            ZombieHero.plugin.meleeCoolDownManager,
            10,
            Tick.sec(0.3),
        ).apply {initialize()}
        val wtfGrenade = Grenade(ZombieHero.plugin.grenadeManager, "普通のグレネード", Tick.sec(1.5)) {loc ->
            val entities = loc.world!!.getNearbyEntities(Util.makeBoundingBox(loc, 5.0)) {
                it is LivingEntity
            }
            loc.world!!.playSound(loc, Sound.ENTITY_GENERIC_EXPLODE, 0.5f,0.5f)
            loc.world!!.spawnParticle(Particle.EXPLOSION_HUGE, loc, 1)
            entities.forEach {entity ->
                entity as LivingEntity
                val dis = entity.location.distance(loc)
                player.sendMessage(dis.toString())
                val baseDamage = 500.0
                val damage = when {
                    dis <= 1 -> baseDamage
                    dis >= 5 -> baseDamage / 5
                    else -> {baseDamage/ dis}
                }
                entity.damage(damage)
            }
        }.apply {initialize()}
        val hitGrenade = HitGrenade(ZombieHero.plugin.hitGrenadeManager, "hitGrenade").apply {initialize()}
        val task = Util.createTask {
            player.inventory.addItem(melee2.createItemStack())
            player.inventory.addItem(wtfGrenade.createItemStack())
            player.inventory.addItem(hitGrenade.createItemStack())
//            gun.playerGiveItem(player)
//            gun2.playerGiveItem(player)
//            gun3.playerGiveItem(player)
//            player.inventory.addItem(grenade.createItemStack())
//            player.inventory.addItem(touchGrenade.createItemStack())
//            player.inventory.addItem(melee.createItemStack())
        }
        Bukkit.getScheduler().runTaskLater(ZombieHero.plugin, task, 1)
    }
}