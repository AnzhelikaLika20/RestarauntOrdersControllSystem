package data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("VisitorAccount")
class VisitorAccount : UserAccount {
    constructor(visitorLogin: String, visitorPassword: String) : super(visitorLogin, visitorPassword)
}