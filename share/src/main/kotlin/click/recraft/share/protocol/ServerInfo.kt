package click.recraft.share.protocol

import com.google.gson.Gson


data class ServerInfo(
    val containerId: String,
    var currentPlayerNum: Int,
    val maxPlayerNum: Int,
    var currentPhase: Int,
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