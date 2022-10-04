package click.recraft.bungee

import click.recraft.share.RedisManager
import click.recraft.share.protocol.ChannelMessage
import click.recraft.share.protocol.MessageType
import click.recraft.share.protocol.ServerInfo
import net.md_5.bungee.api.ProxyServer
import redis.clients.jedis.JedisPubSub
import java.net.InetSocketAddress

class BungeeChannelPubSub(private val proxy: ProxyServer) : JedisPubSub() {
    override fun onSubscribe(channel: String?, subscribedChannels: Int) {
        println("$channel was subscribed")
    }
    private val save: MutableSet<ServerInfo> = mutableSetOf()
    var id = 0

    private fun createServer() {
        val info = ServerInfo(
            "zombiehero$id",
            0,5,0,5
        )
        save.add(info)
        ContainerCreator.create(info.containerId)
        RedisManager.registerServer(info)
        val socketAddress = InetSocketAddress(info.containerId, 25565)
        proxy.servers[info.containerId] = ProxyServer.getInstance().constructServerInfo(info.containerId, socketAddress, info.containerId, false)
        id += 1
    }

    private fun deleteServer(containerID: String) {
        RedisManager.removeValueTransaction("servers", containerID)
        proxy.servers.remove(containerID)
        ContainerCreator.delete(containerID)
    }

    override fun onMessage(channel: String, message: String) {
        println(message)
        val channelMessage = ChannelMessage.fromJson(message)
        when (channelMessage.type) {
            MessageType.CREATE -> createServer()
            MessageType.DELETE -> deleteServer(channelMessage.sourceContainerID)
        }
    }
}
