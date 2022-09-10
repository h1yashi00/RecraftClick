package click.recraft.zombiehero

import click.recraft.zombiehero.item.CustomItemFactory
import org.bukkit.Bukkit
import org.bukkit.entity.EntityType
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
        player.inventory.clear()
//        val monster = Skeleton(player.uniqueId)
//        monster.initialize(player)
//        ZombieHero.plugin.monsterManager.register(player, monster)
        val factory = ZombieHero.plugin.customItemFactory
        val gun = factory.createGun(CustomItemFactory.GunType.AK47)
        val shotGun = factory.createGun(CustomItemFactory.GunType.SHOTGUN)
        val awp = factory.createGun(CustomItemFactory.GunType.AWP)
        val mp5 = factory.createGun(CustomItemFactory.GunType.MP5)
        val hummer = factory.createSword(CustomItemFactory.SwordType.BIG_SWORD)
        val nata    = factory.createSword(CustomItemFactory.SwordType.NORMAL_SWORD)
        val zombieBomb = factory.createGrenade(CustomItemFactory.GrenadeType.ZombieBomb)
        val zombieBombTouch = factory.createGrenade(CustomItemFactory.GrenadeType.ZombieGrenadeTouch)
        val task = Util.createTask {
            player.inventory.helmet = gun.createItemStack()
            player.inventory.setItemInOffHand(gun.createItemStack())
            player.inventory.addItem(gun.createItemStack())
            player.inventory.addItem(shotGun.createItemStack())
            player.inventory.addItem(awp.createItemStack())
            player.inventory.addItem(mp5.createItemStack())
            player.inventory.addItem(hummer.createItemStack())
            player.inventory.addItem(nata.createItemStack())
            player.inventory.addItem(zombieBomb.createItemStack())
            player.inventory.addItem(zombieBombTouch.createItemStack())
        }
        Bukkit.getScheduler().runTaskLater(ZombieHero.plugin, task, 1)
    }
}