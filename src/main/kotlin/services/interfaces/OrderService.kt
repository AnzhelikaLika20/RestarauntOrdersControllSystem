package services.interfaces

import data.models.Order

interface OrderService {
    fun createOrder(order : Order)
    fun changeStatus(id : Int)
    fun containsOrder(id : Int) : Boolean
    fun getAccountById(id : Int) : Order?
}