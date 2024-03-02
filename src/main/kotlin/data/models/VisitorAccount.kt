package data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("VisitorAccount")
data class VisitorAccount(private val visitorLogin: String, private val visitorPassword: String) :
    UserAccount(visitorLogin, visitorPassword)