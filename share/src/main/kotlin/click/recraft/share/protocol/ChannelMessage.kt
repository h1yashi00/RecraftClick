package click.recraft.share.protocol

import com.google.gson.Gson

enum class MessageType{
    CREATE,
    DELETE;
}

data class ChannelMessage(
    val type: MessageType,
    val sourceContainerID: String = System.getenv("SERVER_NAME")
) {
    fun toJson(): String {
        return gson.toJson(this)
    }

    companion object {
        private val gson = Gson()
        fun fromJson(json: String): ChannelMessage {
            return gson.fromJson(json, ChannelMessage::class.java)
        }
    }
    override fun toString(): String {
        throw IllegalAccessException("dont use this!!!!!! use toJson")
    }
}