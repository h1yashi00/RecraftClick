package click.recraft.share.database

import org.bukkit.ChatColor

enum class Quest(val id: Int, val finishNum: Int) {
    KILL_ZOMBIE(1, 15),
    KILL_HUMAN(2, 5),
    PLAY_TIMES(3, 15);
    companion object {
        fun getById(id: Int): Quest? {
            return values().firstOrNull { it.id == id }
        }
    }
    val displayName = "${ChatColor.AQUA}name"
        .lowercase()
        .split("_")
        .joinToString(separator = "")
}