package click.recraft.zombiehero.gun.base

import click.recraft.share.item
import click.recraft.zombiehero.Util
import click.recraft.zombiehero.ZombieHero
import org.bukkit.*
import org.bukkit.block.Block
import org.bukkit.entity.*
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitTask
import org.bukkit.util.BoundingBox
import org.bukkit.util.Vector
import java.util.*

open class GunNoProjectile(
    final override val material: Material,
    final override val data: Short,
    final override val customModelData: Int,
    override val fireRate: Long,
    final override val name: String,
    override val knockBack: Int,
    override val damage: Double,
    final override val maxArmo: Int,
    final override val nameColor: ChatColor = ChatColor.GOLD,
    override val reloadTime: Long,
    override val spread: Double,
    override val recoilY: Float,
    override val recoilZ: Float,
    override val bulletRange: Int,
    override val shootArmoNum: Int = 1,
    override val walkingSpeed: Float = 0.2F,
    private val particle: Particle = Particle.ASH,
    // 一つ一つロードするか確認する｡
    override val reloadOneBullet: Boolean = false
) : GunBase {
    override val fireRateHandle: HashMap<UUID, Long> = hashMapOf()
    override fun getItem(armo: Int, reloading: Boolean): ItemStack {
        val flag = if (reloading) {"▫"} else {"▪"}
        return item(
            material,
            data = data,
            customModelData = customModelData,
            displayName = "${nameColor}$name${ChatColor.WHITE} $flag [$armo]"
        )
    }

    override fun decArmo(gunStats: GunStats, itemStack: ItemStack): ItemStack? {
        val meta = if (itemStack.hasItemMeta()) {
            itemStack.itemMeta ?: return null} else {return null}
        val displayName = "${nameColor}$name${ChatColor.WHITE} ▪ [${gunStats.currentArmo - 1}]"
        meta.setDisplayName(displayName)
        itemStack.itemMeta = meta
        return itemStack
    }

    private fun showBallistic(location: Location) {
        val loc = location.clone()
        loc.world!!.spawnParticle(particle, loc, 1)
    }
    private fun Vector.reverse(): Vector {
        x = -x
        y = -y
        z = -z
        return this
    }
    private fun forceRecoil(player: Player) {
        val pitch = player.location.pitch
        val yaw = player.location.yaw
        if (recoilY == 0F && recoilZ == 0F) return
        player.setRotation(yaw - recoilZ,pitch - recoilY)
    }
    private fun bulletHitBlockEffect(block: Block) {
        val loc = block.location.clone()
        loc.apply { x += 0.5; y += 0.5; z+=0.5 }
        loc.world!!.spawnParticle(Particle.BLOCK_CRACK, loc, 100, block.type.createBlockData())
    }
    private fun getGunStats(player: Player): GunStats? {
        val item = player.inventory.itemInMainHand
        var gun: GunBase? = null
        ZombieHero.plugin.guns.forEach {
            if (it.isGun(item) != null) {
                gun = it
            }
        }
        return gun?.getCurrentGunStats(item)
    }
    init {
        val task: (BukkitTask) -> Unit = {
            reloadCheck.iterator().forEach {
                val uuid = it.key
                val data = it.value
                val player = Bukkit.getPlayer(uuid) ?: return@forEach
                val currentHoldItem = player.inventory.itemInMainHand
                val passedTime = System.currentTimeMillis() - data.tick
                // プレイヤーが武器を切り替えていないかチェックする
                if (data.item != currentHoldItem) {
                    reloadCheck.remove(uuid)
                    return@forEach
                }
                if (passedTime < reloadTime) {
                    // まだReloadが完了していない
                    return@forEach
                }
                // reload終了!
                reloadEnd(player)
                player.inventory.setItemInMainHand(getItem())
            }
        }
        Bukkit.getScheduler().runTaskTimer(ZombieHero.plugin, task, 1, 5)
    }
    // 5 tickごとにチェックをして､アイテムが変化していないか確認して､していた場合は､やめる｡
    private data class GunReloadPlayerData(val tick: Long, val item: ItemStack)
    private val reloadCheck = hashMapOf<UUID, GunReloadPlayerData>()
    private fun reloadStart(player: Player) {
        player.playSound(player.location, Sound.BLOCK_WOODEN_DOOR_OPEN, 0.5f, 2f)
        // プレイヤーがリロード中なので､返す
        if (reloadCheck.contains(player.uniqueId)) { return }
        player.inventory.setItemInMainHand(getItem(armo = 0, reloading = true))
        reloadCheck[player.uniqueId] = GunReloadPlayerData(System.currentTimeMillis(), player.inventory.itemInMainHand)
    }
    private fun reloadEnd(player: Player) {
        player.playSound(player.location, Sound.BLOCK_WOODEN_DOOR_CLOSE, 0.5f, 2f)
        player.inventory.setItemInMainHand(getItem(reloading = false))
    }
    private fun updateItemGun(player: Player): Boolean {
        val gunStats = getGunStats(player) ?: return false
        if (gunStats.currentArmo == 0) {
            // 弾薬が0でreloadフラグが立っていない場合はここ↑↓
            if (!gunStats.reloading) {
                reloadStart(player)
                return true
            }
        }
        else {
            gunStats.gun.decArmo(gunStats, player.inventory.itemInMainHand)
        }
        player.updateInventory()
        return gunStats.reloading
    }

    private fun rand(spread: Double): Double {
        val rangeMin = -spread
        val rangeMax =  spread
        return rangeMin + (rangeMax - rangeMin) * Random().nextDouble()
    }
    private fun shootArmo(bullet: Location, player: Player) {
        bullet.apply { direction = Vector(direction.x + rand(spread), direction.y + rand(spread), direction.z + rand(spread)) }
        (0..(bulletRange*2)).forEach{ count ->
            showBallistic(bullet)
            bullet.apply { x += (direction.x / 2) ; y += (direction.y / 2); z += (direction.z /2)}
            if (bullet.block.type.isSolid) {
                bulletHitBlockEffect(bullet.block)
                return
            }
            val bulletBoundingBox = Util.makeBoundingBox(bullet,0.1)
            val entities = bullet.world!!.getNearbyEntities(bulletBoundingBox)
            if (entities.isNotEmpty()) {
                if (entities.contains(player)) { return@forEach }
                val entity = entities.first()
                if (entity.isDead) { return@forEach }
                if (entity is ArmorStand) return@forEach
                // コンパイルくんに知らせるためのやつ
                if (entity !is LivingEntity) return@forEach
                val dmg = if (isHeadLocation(bulletBoundingBox, entity.eyeLocation)) {
                    damage * 2
                }
                else {
                    damage
                }
                entity.damage(dmg)
                if (entity is Zombie) {
                    entity.target = player
                }
                val dir = player.location.direction.multiply(0.5)
                entity.velocity = Vector(dir.x,  0.1, dir.z)
                entity.noDamageTicks = 0
                return
            }
        }
    }
    override fun gunAction(player: Player) {
        if (updateItemGun(player)) {
            return
        }
        player.world.playSound(player.location, Sound.ENTITY_IRON_GOLEM_HURT, 1f, 3F)
        val dir = player.location.direction
        player.location.direction = dir.multiply(1)
        val bullet = player.location.clone().apply {y+=player.eyeHeight }
        (1..shootArmoNum).forEach {
            shootArmo(bullet.clone(), player)
        }
        forceRecoil(player)
    }


    private fun isHeadLocation(bulletBoundingBox: BoundingBox, headLoc: Location): Boolean {
        val headSize = 0.4
        val x1 = headLoc.x - headSize
        val y1 = headLoc.y - headSize
        val z1 = headLoc.z - headSize

        val x2 = headLoc.x + headSize
        val y2 = headLoc.y + headSize
        val z2 = headLoc.z + headSize
        return BoundingBox(x1,y1,z1, x2,y2,z2).contains(bulletBoundingBox)
    }
}