package click.recraft.lobby

import click.recraft.share.protocol.Channel
import redis.clients.jedis.JedisPubSub

class PubSub: JedisPubSub() {
    override fun onMessage(channel: String, message: String) {
        when (Channel.valueOf(channel)) {
            Channel.Bungee -> {} // ignore
            Channel.Server -> {
            }
        }
    }

    override fun onSubscribe(channel: String, subscribedChannels: Int) {
        println("$channel is subscribed!!!")
    }
}