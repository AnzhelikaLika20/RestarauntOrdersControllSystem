package presentation.interfaces

import services.models.Response

interface AdminMenu {
    fun displayMenuOptions()
    fun dealWithUser(): Response
}