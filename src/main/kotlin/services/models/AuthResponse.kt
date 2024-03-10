package services.models

import data.models.UserAccount

data class AuthResponse(val status: ResponseCode, val hint: String, val account: UserAccount?)