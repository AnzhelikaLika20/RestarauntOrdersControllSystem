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

    private fun createListOfDishes(dishNames: List<String>): List<OrderDish> {
        val dishes: MutableList<OrderDish> = mutableListOf()
        for (dishName in dishNames) {
            val dishResponse = menuService.getDishByName(dishName)
            if (dishResponse.status == ResponseCode.Success) {
                if(dishResponse.dish != null && dishResponse.dish.amount > 0) {
                    val dish = dishResponse.dish
                    val newDish = OrderDish(dish.name, dish.price, dish.difficulty)
                    menuService.setAmountOfDish(dishName, dish.amount - 1)
                    dishes.addLast(newDish)
                } else {
                    println("There is no any $dishName left\n")
                }
            } else
                println(dishResponse.hint)
        }
        return dishes.toList()
    }

    override fun createOrder(dishNames: List<String>, account: String): OrderResponse {
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

    override fun addDishIntoOrder(orderId: String, dishNames: List<String>): OrderResponse {
        if (!orderRepository.containsOrder(orderId))
            return OrderResponse(ResponseCode.BadRequest, "There is no order with such id\n", null)
        val dishes: List<OrderDish> = createListOfDishes(dishNames)
        if (dishes.isEmpty())
            return OrderResponse(ResponseCode.BadRequest, "There is no dishes chosen to modify order\n", null)
        val order = orderRepository.addDishIntoOrder(orderId, dishes)
        return OrderResponse(ResponseCode.Success, "Order $orderId was modified\n", order)
    }

    override fun payOrder(uuid: String, orderId : String): OrderResponse {
        val readyForPaymentOrdersOfUser = getReadyForPaymentOrdersOfUser(uuid)
        if(!readyForPaymentOrdersOfUser.contains(orderId))
            return OrderResponse(ResponseCode.BadRequest, "There is no such order amount orders ready to pay", null)
        orderRepository.changeStatus(orderId, OrderStatus.Paid)
        val order = orderRepository.getOrderById(uuid)
        return OrderResponse(ResponseCode.Success, "Order $orderId was successfully paid", order)
    }

    override fun cancelOrder(uuid: String, orderId : String): OrderResponse {
        val preparingOrdersOfUser = getPreparingOrdersOfUser(uuid)
        if(!preparingOrdersOfUser.contains(orderId))
            return OrderResponse(ResponseCode.BadRequest, "There is no such order amount preparing orders", null)
        orderRepository.changeStatus(orderId, OrderStatus.Cancelled)
        val order = orderRepository.getOrderById(uuid)
        return OrderResponse(ResponseCode.Success, "Order $orderId was successfully canceled", order)
    }

    override fun getActiveOrdersOfUser(account: String): List<String> {
        return orderRepository.getActiveOrdersOfUser(account)
    }

    override fun getReadyForPaymentOrdersOfUser(account: String): List<String> {
        return orderRepository.getReadyForPaymentOrdersOfUser(account)
    }

    override fun getPreparingOrdersOfUser(account: String): List<String> {
        return orderRepository.getPreparingOrdersOfUser(account)
    }

}