package services

import data.Interfaces.OrderRepository
import data.models.Dish
import data.models.Order
import data.models.OrderDish
import data.models.OrderStatus
import data.repositories.OrderRepositoryImpl
import di.DI.menuService
import services.interfaces.OrderService
import services.models.OrderResponse
import services.models.ResponseCode
import java.time.LocalTime
import java.util.*

class OrderServiceImpl(private val orderRepository: OrderRepositoryImpl) : OrderService {

    private fun createListOfDishes(dishNames : List<String>) : List<OrderDish> {
        val dishes: MutableList<OrderDish> = mutableListOf()
        for (dishName in dishNames) {
            val dishResponse = menuService.getDishByName(dishName)
            if (dishResponse.status == ResponseCode.Success && dishResponse.dish != null) {
                val dish = dishResponse.dish
                val newDish = OrderDish(dish.name, dish.price, dish.difficulty)
                menuService.setAmountOfDish(dishName, dish.amount - 1)
                dishes.addLast(newDish)
            }
            else
                println(dishResponse.hint)
        }
        return dishes.toList()
    }
    override fun createOrder(dishNames : List<String>, account : String) : OrderResponse {
        val dishes: List<OrderDish> = createListOfDishes(dishNames)
        if (dishes.isEmpty()) {
            return OrderResponse(ResponseCode.BadRequest, "It is impossible to create an empty order\n", null)
        }
        val uniqueId: String = UUID.randomUUID().toString()
        var totalPrice = 0.0
        dishes.forEach { x -> totalPrice += x.price }
        val maxDifficulty: Int = dishes.map { it.difficulty }.maxOf { it }
        val order =
            Order(uniqueId, dishes, OrderStatus.Preparing, totalPrice, maxDifficulty, LocalTime.now(), account)
        orderRepository.addOrder(order)
        return OrderResponse(ResponseCode.Success, "Order was successfully created\n\n", order)
    }

    override fun addDishIntoOrder(uuid: String, dishNames: List<String>): OrderResponse {
        if(!orderRepository.containsOrder(uuid))
            return OrderResponse(ResponseCode.BadRequest, "There is no order with such id\n", null)
        val dishes: List<OrderDish> = createListOfDishes(dishNames)
        if (dishes.isEmpty())
            return OrderResponse(ResponseCode.BadRequest, "It is impossible to create an empty order\n", null)
        val order = orderRepository.addDishIntoOrder(uuid, dishes)
        return OrderResponse(ResponseCode.Success, "Order $uuid was modified\n", order)
    }

    override fun payOrder(uuid: String): OrderResponse {
        TODO("Not yet implemented")
    }

    override fun cancelOrder(uuid: String): OrderResponse {
        TODO("Not yet implemented")
    }

    override fun getActiveOrdersOfUser(account: String): List<String> {
        return orderRepository.getActiveOrdersOfUser(account)
    }

}