package data.Interfaces

import data.models.Order
import data.models.OrderDish
import data.models.OrderStatus
import data.models.UserAccount

interface OrderRepository {
    fun storeInfo(info: String)
    fun loadInfo() : String
    fun addOrder(order : Order)
    fun containsOrder(id : String) : Boolean
    fun getActiveOrdersOfUser(login : String) : List<String>
    fun addDishIntoOrder(uuid: String, dishes: List<OrderDish>) : Order?
    fun getOrderById(uuid : String) : Order?
    fun getReadyForPaymentOrdersOfUser(login : String) : List<String>
    fun changeStatus(uuid: String, status : OrderStatus)
    fun getPreparingOrdersOfUser(login : String) : List<String>
}