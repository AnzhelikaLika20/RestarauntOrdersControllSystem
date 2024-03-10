package services.interfaces

import presentation.models.Role
import services.models.AuthResponse

interface AuthService {
    fun loginUser(login: String, password: String): AuthResponse
    fun registerUser(login: String, password: String, role: Role): AuthResponse
}