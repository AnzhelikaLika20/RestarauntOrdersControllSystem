package presentation.interfaces

import services.models.AdminResponse

interface DishMenu {
    fun displayMenuOptions()
    fun dealWithUser() : AdminResponse
}