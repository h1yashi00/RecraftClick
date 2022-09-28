package click.recraft.share.protocol

enum class Channel {
    Bungee,
    Server;

    override fun toString(): String {
        return name.lowercase()
    }
}