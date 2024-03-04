package data.models

import kotlinx.serialization.Serializable

@Serializable
sealed class UserAccount(
    val login: String,
    val password: String,
    var isActive: Boolean = false
)
