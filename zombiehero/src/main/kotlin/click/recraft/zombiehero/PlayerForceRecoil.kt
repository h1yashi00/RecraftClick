package click.recraft.zombiehero


import net.minecraft.network.protocol.game.PacketPlayOutPosition
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer
import org.bukkit.entity.Player

object PlayerForceRecoil {
    private val FLAGS = setOf(
        PacketPlayOutPosition.EnumPlayerTeleportFlags.a,
        PacketPlayOutPosition.EnumPlayerTeleportFlags.b,
        PacketPlayOutPosition.EnumPlayerTeleportFlags.c,
        PacketPlayOutPosition.EnumPlayerTeleportFlags.d,
        PacketPlayOutPosition.EnumPlayerTeleportFlags.e,
    )

    fun sendRecoilPacket(player: Player, yaw: Float, pitch: Float) {
        val packet = PacketPlayOutPosition(0.0, 0.0, 0.0, yaw, pitch, FLAGS, 0, false)
        val entityPlayer = (player as CraftPlayer).handle
        entityPlayer.b.a(packet)
    }
}