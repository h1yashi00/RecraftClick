package click.recraft.share.protocol

import com.google.gson.Gson

enum class Request {
    Create,
    Delete;
    companion object {
        private val gson = Gson()
        fun fromJson(json: String): Request {
            return gson.fromJson(json, Request::class.java)!!
        }
    }
}

data class ServerInfo(
    val containerId: String,
    val currentPlayerNum: Int,
    val maxPlayerNum: Int,
    val currentPhase: Int,
    val maxPhase: Int,
) {
    companion object {
        private val gson = Gson()
        fun fromJson(json: String): ServerInfo {
            return gson.fromJson(json, ServerInfo::class.java)!!
        }
    }
    fun toJson(): String {
        return gson.toJson(this)!!
    }
}