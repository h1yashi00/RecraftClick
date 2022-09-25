package click.recraft.zombiehero.player

import click.recraft.zombiehero.item.melee.Sword
import click.recraft.zombiehero.task.OneTickTimerTask
import kotlin.collections.HashSet

object SwordManager: OneTickTimerTask {
    private val attackingSwords : HashSet<Sword> = hashSetOf()
    fun registerAttacking(sword: Sword) {
        attackingSwords.add(sword)
    }
    fun isAttacking(sword: Sword): Boolean {
        return attackingSwords.contains(sword)
    }
    fun remove(sword: Sword) {
        attackingSwords.remove(sword)
    }

    override fun loopEveryOneTick() {
        attackingSwords.iterator().forEach { sword->
            sword.oneTick()
        }
    }
}