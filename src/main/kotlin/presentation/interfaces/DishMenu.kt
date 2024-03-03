package presentation.interfaces

import services.models.DishResponse

interface DishMenu {
    fun displayMenuOptions()
    fun dealWithUser() : DishResponse
}