package presentation.interfaces

import services.models.DishResponse

interface AdminMenu {
    fun displayMenuOptions()
    fun dealWithUser() : DishResponse
}