package services

import data.Interfaces.OrderRepository
import data.models.Order
import data.repositories.OrderRepositoryImpl
import services.interfaces.OrderService

class OrderServiceImpl(private val orderRepository: OrderRepositoryImpl) : OrderService {
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