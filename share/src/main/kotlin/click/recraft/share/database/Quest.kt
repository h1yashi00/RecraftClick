package click.recraft.share.database

import org.bukkit.ChatColor

enum class Quest(val id: Int, val finishNum: Int, _displayName: String, val giveCoin: Int) {
    KILL_ZOMBIE(1, 15, "モンスターを倒す", 1000),
    KILL_HUMAN(2,  5,  "人間を感染させる", 1000),
    PLAY_TIMES(3, 15,   "プレイした数", 1500);
    companion object {
        fun getById(id: Int): Quest? {
            return values().firstOrNull { it.id == id }
        }
    }
    val displayName = "${ChatColor.AQUA}${_displayName}"
}