package services.interfaces

import data.models.Order
import services.models.OrderResponse
import java.util.UUID

interface OrderService {
    fun createOrder(dishNames : List<String>, account : String) : OrderResponse
    fun addDishIntoOrder(uuid: String, dishNames : List<String>) : OrderResponse
    fun payOrder(uuid: String) : OrderResponse
    fun cancelOrder(uuid: String) : OrderResponse
    fun getActiveOrdersOfUser(account : String) : List<String>

}