package click.recraft.zombiehero.monster

import org.bukkit.Bukkit
import org.bukkit.inventory.ItemStack

interface Skill {
    val monster: Monster
    val item: ItemStack
    val coolDownTime: Int
    val index: Int
    var coolDown: Int
    var active: Boolean
    fun active()
    fun coolDownCheck(): Boolean {
        return coolDown <= 0
    }
    fun isSame(itemStack: ItemStack?): Boolean {
        itemStack?: return false
        val skillItem = item
        val skillMeta = skillItem.itemMeta
        if (skillItem.type == itemStack.type && skillItem.itemMeta == skillMeta) {
            return true
        }
        return false
    }
    fun passOneSec() {
        val player = Bukkit.getPlayer(monster.playerUUID) ?: return
        if (coolDown <= 0) {
            return
        }
        val itemAmount = if (coolDown <= 0) {
            1
        }
        else {
            coolDown
        }
        val inv = player.inventory
        inv.maxStackSize = 127
        inv.setItem(index, item.clone().apply { amount = itemAmount})
        coolDown += -1
    }
}