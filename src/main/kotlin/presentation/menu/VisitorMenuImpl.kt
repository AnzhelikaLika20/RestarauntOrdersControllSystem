package presentation.menu

import presentation.interfaces.VisitorMenu
import services.interfaces.MenuService
import services.interfaces.OrderService
import services.models.DishResponse

class VisitorMenuImpl(private val orderService: OrderService) : VisitorMenu {
    override fun displayMenuOptions() {
        TODO("Not yet implemented")
    }

    override fun dealWithUser(): DishResponse {
        TODO("Not yet implemented")
    }

    override fun getCurrentOrderStatus(id: Int) {
        TODO("Not yet implemented")
    }

    override fun createOrder() {
        TODO("Not yet implemented")
    }

    override fun addDishIntoExistingOrder(id: Int) {
        TODO("Not yet implemented")
    }

    override fun payOrder(id: Int) {
        TODO("Not yet implemented")
    }
}