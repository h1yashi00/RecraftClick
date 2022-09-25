package click.recraft.zombiehero


import net.minecraft.network.protocol.game.PacketPlayOutAnimation
import net.minecraft.network.protocol.game.PacketPlayOutPosition
import org.bukkit.Bukkit
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftEntity
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player

object UseNms {
    private val FLAGS = setOf(
        PacketPlayOutPosition.EnumPlayerTeleportFlags.a,
        PacketPlayOutPosition.EnumPlayerTeleportFlags.b,
        PacketPlayOutPosition.EnumPlayerTeleportFlags.c,
        PacketPlayOutPosition.EnumPlayerTeleportFlags.d,
        PacketPlayOutPosition.EnumPlayerTeleportFlags.e,
    )

    fun sendRecoilPacket(player: Player, yaw: Float, pitch: Float) {
        val packet = PacketPlayOutPosition(0.0, 0.0, 0.0, yaw, pitch, FLAGS, 0, true)
        val entityPlayer = (player as CraftPlayer).handle
        entityPlayer.b.a(packet)
    }

    fun sendDamageAnimationPacket(livingEntity: LivingEntity) {
        val nmsEntity = (livingEntity as CraftEntity).handle
        val packet = PacketPlayOutAnimation((nmsEntity), 1)
        Bukkit.getOnlinePlayers().forEach {(it as CraftPlayer).handle.b.a(packet) }
    }
}