package data.repositories

import data.Interfaces.OrderRepository
import data.models.Order

class OrderRepositoryImpl(private val pathToSerializedStorage: String) : OrderRepository {
    override fun storeInfo(info: String) {
        TODO("Not yet implemented")
    }

    override fun loadInfo(): String {
        TODO("Not yet implemented")
    }

    override fun createOrder(order: Order) {
        TODO("Not yet implemented")
    }

    override fun changeStatus(id: Int) {
        TODO("Not yet implemented")
    }

    override fun containsOrder(id: Int): Boolean {
        TODO("Not yet implemented")
    }

    override fun getAccountById(id: Int): Order? {
        TODO("Not yet implemented")
    }
}