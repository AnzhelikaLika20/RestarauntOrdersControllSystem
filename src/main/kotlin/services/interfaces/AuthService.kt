package services.interfaces

import services.models.AuthResponse
import presentation.models.Role

interface AuthService {
    fun loginUser(login : String, password : String) : AuthResponse
    fun registerUser(login : String, password : String, role : Role) : AuthResponse
}