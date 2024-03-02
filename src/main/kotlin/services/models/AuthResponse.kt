package services.models

import data.models.UserAccount

data class AuthResponse (val status : Int, val hint : String, val account : UserAccount?)