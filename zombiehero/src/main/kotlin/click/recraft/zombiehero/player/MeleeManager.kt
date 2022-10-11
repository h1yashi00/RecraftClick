package click.recraft.zombiehero.player

import click.recraft.zombiehero.item.melee.Melee
import click.recraft.zombiehero.task.OneTickTimerTask
import kotlin.collections.HashSet

object MeleeManager: OneTickTimerTask {
    private val attackingMelees : HashSet<Melee> = hashSetOf()
    fun registerAttacking(melee: Melee) {
        attackingMelees.add(melee)
    }
    fun isAttacking(melee: Melee): Boolean {
        return attackingMelees.contains(melee)
    }
    fun remove(melee: Melee) {
        attackingMelees.remove(melee)
    }

    override fun loopEveryOneTick() {
        attackingMelees.iterator().forEach { melee->
            melee.oneTick()
        }
    }
}