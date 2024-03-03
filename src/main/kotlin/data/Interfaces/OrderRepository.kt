package data.Interfaces

import data.models.Order
import data.models.UserAccount

interface OrderRepository {
    fun storeInfo(info: String)
    fun loadInfo() : String
    fun createOrder(order : Order)
    fun changeStatus(id : Int)
    fun containsOrder(id : Int) : Boolean
    fun getAccountById(id : Int) : Order?
}