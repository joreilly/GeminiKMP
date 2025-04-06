package core.models

data class ChatMessage(
    val id: Long? = null,
    val message: String,
    val sender: String,
    val time: String
)
