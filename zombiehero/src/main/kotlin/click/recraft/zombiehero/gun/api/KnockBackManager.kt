package click.recraft.zombiehero.gun.api

import click.recraft.zombiehero.Util
import click.recraft.zombiehero.ZombieHero
import org.bukkit.Bukkit
import org.bukkit.entity.LivingEntity
import org.bukkit.util.Vector

class KnockBackManager {
    private val saveEntity = hashMapOf<LivingEntity, Data>()
    private data class Data(val direction: Vector, val expireTime: Long, var knockBackAccumulates: Double) {
    }
    init {
        val task = Util.createTask {
            val now = System.currentTimeMillis()
            saveEntity.iterator().forEach {(entity,data) ->
                if (entity.isDead) return@forEach
                 val checkTime = now - data.expireTime
                if (checkTime < 0) {
                    val dir = data.direction.multiply(data.knockBackAccumulates)
                    val vertical = if (data.knockBackAccumulates > 1.5) {1.5} else {data.knockBackAccumulates}
                    entity.velocity = dir.apply { y += vertical }
                    saveEntity.remove(entity)
                    return@forEach
                }
            }
        }
        Bukkit.getScheduler().runTaskTimer(ZombieHero.plugin, task, 10, 1)
    }
    fun register(livingEntity: LivingEntity, dir: Vector, knockBackValue: Double, time: Long) {
        val data = saveEntity[livingEntity]
        if (data == null) {
            val expireTime = System.currentTimeMillis() + time
            saveEntity[livingEntity] = Data(dir, expireTime, knockBackValue)
            return
        }
        data.knockBackAccumulates += knockBackValue
        saveEntity[livingEntity] = data
    }
}
