package data.Interfaces

import data.models.Order
import data.models.OrderDish
import data.models.OrderStatus

interface OrderRepository {
    fun addOrder(order : Order)
    fun containsOrder(id : String) : Boolean
    fun getActiveOrdersOfUser(login : String) : List<String>
    fun addDishesIntoOrder(uuid: String, dishes: List<OrderDish>) : Order?
    fun getOrderById(uuid : String) : Order?
    fun getReadyForPaymentOrdersOfUser(login : String) : List<String>
    fun changeStatus(uuid: String, status : OrderStatus)
    fun getPaidOrdersOfUser(login: String): List<String>
    fun getAllOrders() : List<Order>
}