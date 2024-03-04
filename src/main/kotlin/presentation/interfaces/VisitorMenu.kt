package presentation.interfaces

import data.models.VisitorAccount
import services.models.OrderResponse

interface VisitorMenu {
    fun displayMenuOptions()
    fun dealWithUser(account : VisitorAccount) : OrderResponse
    fun createOrder(account : VisitorAccount) : OrderResponse
    fun addDishIntoOrder(account: VisitorAccount) : OrderResponse
    fun payOrder(account: VisitorAccount) : OrderResponse
    fun exit() : OrderResponse
}