package services

import data.interfaces.OrderRepository
import data.interfaces.StatisticsRepository
import data.models.Order
import data.models.OrderDish
import data.models.OrderStatus
import data.models.Statistics
import di.DI.menuService
import services.interfaces.OrderService
import services.models.OrderResponse
import services.models.ResponseCode
import java.time.LocalDateTime
import java.util.*
import kotlin.concurrent.thread

class OrderServiceImpl(
    private val orderRepository: OrderRepository,
    private val statisticRepository: StatisticsRepository
) : OrderService {

    private val orderThreads = mutableMapOf<String, Thread>()

    init {
        for (order in orderRepository.getAllOrders().filter { it.status == OrderStatus.Preparing }) {
            executeOrder(order)
        }
    }

    private fun createListOfDishes(dishNames: List<String>): List<OrderDish> {
        val dishes: MutableList<OrderDish> = mutableListOf()
        for (dishName in dishNames) {
            val dishResponse = menuService.getDishByName(dishName)
            if (dishResponse.status == ResponseCode.Success) {
                if (dishResponse.dish != null && dishResponse.dish.amount > 0) {
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
            Order(uniqueId, dishes, OrderStatus.Preparing, totalPrice, maxDifficulty, LocalDateTime.now(), account)
        orderRepository.addOrder(order)
        executeOrder(order)
        return OrderResponse(ResponseCode.Success, "Order was successfully created\n\n", order)
    }

    private fun executeOrder(order: Order) {
        val cookingThread = thread(start = false) {
            try {
                Thread.sleep(1000L * order.preparingDurationInMinutes)
                orderRepository.changeStatus(order.id, OrderStatus.Ready)
                orderThreads.remove(order.id)
            } catch (_: InterruptedException) {
                return@thread
            }
        }

        orderThreads[order.id] = cookingThread
        cookingThread.start()
    }

    override fun addDishIntoOrder(orderId: String, dishNames: List<String>): OrderResponse {
        if (!orderRepository.containsOrder(orderId))
            return OrderResponse(ResponseCode.BadRequest, "There is no order with such id\n", null)
        val dishes: List<OrderDish> = createListOfDishes(dishNames)
        if (dishes.isEmpty())
            return OrderResponse(ResponseCode.BadRequest, "There is no dishes chosen to modify order\n", null)
        val order = orderRepository.addDishesIntoOrder(orderId, dishes)
        return OrderResponse(ResponseCode.Success, "Order $orderId was modified\n", order)
    }

    override fun payOrder(login: String, orderId: String): OrderResponse {
        val readyForPaymentOrdersOfUser = getReadyForPaymentOrdersOfUser(login)
        if (!readyForPaymentOrdersOfUser.contains(orderId))
            return OrderResponse(ResponseCode.BadRequest, "There is no such order among orders ready to pay", null)
        orderRepository.changeStatus(orderId, OrderStatus.Paid)
        val order = orderRepository.getOrderById(orderId) ?: return OrderResponse(
            ResponseCode.BadRequest,
            "There is no such order among orders ready to pay",
            null
        )
        val statistic = Statistics(statisticRepository.getStatistics().revenue + order.totalPrice)
        statisticRepository.saveStatistics(statistic)
        return OrderResponse(ResponseCode.Success, "Order $orderId was successfully paid\n", order)
    }

    override fun getAllOrders(): List<Order> {
        return orderRepository.getAllOrders()
    }

    override fun cancelOrder(login: String, orderId: String): OrderResponse {
        val preparingOrdersOfUser = getPreparingOrdersOfUser(login)
        if (!preparingOrdersOfUser.contains(orderId))
            return OrderResponse(ResponseCode.BadRequest, "There is no such order amount preparing orders", null)
        orderThreads[orderId]?.interrupt()
        orderRepository.changeStatus(orderId, OrderStatus.Cancelled)
        val order = orderRepository.getOrderById(orderId)
        return OrderResponse(ResponseCode.Success, "Order $orderId was successfully canceled", order)
    }

    override fun getActiveOrdersOfUser(account: String): List<String> {
        return orderRepository.getActiveOrdersOfUser(account)
    }

    override fun getPaidOrdersOfUser(account: String): List<String> {
        return orderRepository.getPaidOrdersOfUser(account)
    }

    override fun getReadyForPaymentOrdersOfUser(account: String): List<String> {
        return orderRepository.getReadyForPaymentOrdersOfUser(account)
    }

    override fun getPreparingOrdersOfUser(account: String): List<String> {
        return orderRepository.getActiveOrdersOfUser(account)
    }

    override fun clearOrders() {
        for (order in orderThreads)
            order.value.interrupt()
        orderThreads.clear()
    }

}