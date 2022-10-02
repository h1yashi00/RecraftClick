package click.recraft.share

import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream
import java.io.IOException

object TeleportServer {
    fun send(player: Player, server: String, plugin: JavaPlugin) {
        val byteArrayOutputStream = ByteArrayOutputStream()
        val dataOutputStream      = DataOutputStream(byteArrayOutputStream)
        try {
            dataOutputStream.writeUTF("Connect")
            dataOutputStream.writeUTF(server)
        } catch(ex: IOException) {
            ex.printStackTrace()
        }
        player.sendPluginMessage(plugin, "BungeeCord", byteArrayOutputStream.toByteArray())
        player.sendMessage("${ChatColor.GRAY}Connecting")
    }
}