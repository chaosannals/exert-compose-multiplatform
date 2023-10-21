package server.messages

import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class User(
    val id: Long,
    val name: String,
    val account: String,
)