package presentation.interfaces

import services.models.AuthResponse

interface AuthMenu {
    fun displayMenuOptions()
    fun dealWithUser(): AuthResponse
}