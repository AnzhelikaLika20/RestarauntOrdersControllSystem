package data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("AdminAccount")
class AdminAccount : UserAccount{
    constructor(adminLogin: String, adminPassword: String) : super(adminLogin, adminPassword)
}
