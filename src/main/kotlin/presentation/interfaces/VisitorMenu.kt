package presentation.interfaces

import services.models.DishResponse

interface VisitorMenu {
    fun displayMenuOptions()
    fun dealWithUser() : DishResponse
    fun getCurrentOrderStatus(id : Int)
    fun createOrder()
    fun addDishIntoExistingOrder(id : Int)
    fun payOrder(id: Int)
}