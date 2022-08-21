package click.recraft.zombiehero.gun.base

import click.recraft.share.item
import click.recraft.zombiehero.ZombieHero
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.entity.Snowball
import org.bukkit.inventory.ItemStack
import org.bukkit.metadata.FixedMetadataValue
import java.util.*

open class GunProjectile (
    final override val material: Material,
    final override val data: Short,
    final override val customModelValue: Int,
    private val projectile: Class<Snowball>,
    private val bulletSpeed: Double,
    override val fireRate: Long,
    override val name: String,
    override val knockBack: Int,
    override val damage: Double,
    override val maxArmo: Int,
    override val nameColor: ChatColor,
    override val reloadTime: Long,
    override val spread: Double,
    override val recoilY: Float,
    override val recoilZ: Float,
    override val bulletRange: Int,
    override val shootArmoNum: Int = 0,
    override val walkingSpeed: Float
) : GunBase {
    override val fireRateHandle: HashMap<UUID, Long> = hashMapOf()

    private val item = item(material, data = data, customModelData = customModelValue)

    override fun gunAction(player: Player) {
        val projectileEntity = player.launchProjectile(projectile)
        val damageKey = "${name}_bullet_damage"
        val damage = 10
        projectileEntity.setMetadata(damageKey, FixedMetadataValue(ZombieHero.plugin, damage))
        val velocityKey = "${name}_bullet_velocity"
        val velocity = 2.0
        projectileEntity.setMetadata(velocityKey, FixedMetadataValue(ZombieHero.plugin, velocity))
        val dir = player.location.direction.apply {multiply(bulletSpeed)}
        projectileEntity.velocity = dir
    }

    override fun getItem(armo: Int, reloading: Boolean): ItemStack {
        TODO("Not yet implemented")
    }


    override fun decArmo(gunStats: GunStats, itemInMainHand: ItemStack): ItemStack? {
        TODO("Not yet implemented")
    }

    override val reloadOneBullet: Boolean
        get() = TODO("Not yet implemented")
}