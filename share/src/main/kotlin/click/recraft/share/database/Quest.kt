package click.recraft.share.database

import org.bukkit.ChatColor

enum class Quest(val id: Int, val finishNum: Int, _displayName: String) {
    KILL_ZOMBIE(1, 15, "モンスターを倒す"),
    KILL_HUMAN(2,  5,  "人間を感染させる"),
    PLAY_TIMES(3, 15,   "プレイした数");
    companion object {
        fun getById(id: Int): Quest? {
            return values().firstOrNull { it.id == id }
        }
    }
    val displayName = "${ChatColor.WHITE}${ChatColor.BOLD}デイリークエスト ${ChatColor.AQUA}${_displayName}"
}