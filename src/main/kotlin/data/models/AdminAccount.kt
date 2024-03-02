package data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("AdminAccount")
data class AdminAccount(private val adminLogin: String, private val adminPassword: String) :
    UserAccount(adminLogin, adminPassword)