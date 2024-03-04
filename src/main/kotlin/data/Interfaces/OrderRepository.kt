package data.Interfaces

import data.models.Order
import data.models.OrderDish
import data.models.UserAccount

interface OrderRepository {
    fun storeInfo(info: String)
    fun loadInfo() : String
    fun addOrder(order : Order)
    fun containsOrder(id : String) : Boolean
    fun getActiveOrdersOfUser(login : String) : List<String>
    fun addDishIntoOrder(uuid: String, dishes: List<OrderDish>) : Order?
}