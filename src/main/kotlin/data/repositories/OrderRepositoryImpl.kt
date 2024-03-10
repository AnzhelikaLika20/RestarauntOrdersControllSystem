package data.repositories

import data.interfaces.OrderRepository
import data.models.Order
import data.models.OrderDish
import data.models.OrderStatus
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import services.utils.LocalDateTimeSerializer
import java.io.File
import java.io.FileNotFoundException
import java.time.LocalDateTime

class OrderRepositoryImpl(private val pathToSerializedStorage: String) : OrderRepository {
    private val json = Json { prettyPrint = true }
    private fun storeInfo(listOrders: List<Order>) {
        val file = File(pathToSerializedStorage)
        val serializedInfo = json.encodeToString(listOrders)
        file.writeText(serializedInfo)
    }

    private fun loadInfo(): String {
        val file = File(pathToSerializedStorage)
        try {
            return file.readText()
        } catch (_: FileNotFoundException) {
            file.createNewFile()
            return ""
        }
    }

    override fun addOrder(order: Order) {
        val listOfOrders = getAllOrders().toMutableList()
        listOfOrders.addLast(order)
        storeInfo(listOfOrders)
    }

    override fun containsOrder(orderId: String): Boolean {
        val listOfOrders = getAllOrders()
        return listOfOrders.any { x -> x.id == orderId }
    }

    override fun getActiveOrdersOfUser(login: String): List<String> {
        val listOfOrders = getAllOrders()
        return listOfOrders.filter { x -> x.visitorLogin == login && x.status == OrderStatus.Preparing }.map { it.id }
    }

    override fun getPaidOrdersOfUser(login: String): List<String> {
        val listOfOrders = getAllOrders()
        return listOfOrders.filter { x -> x.visitorLogin == login && x.status == OrderStatus.Paid }.map { it.id }
    }

    override fun getAllOrders(): List<Order> {
        val infoFromFile = loadInfo()
        val json = Json {
            serializersModule = SerializersModule {
                contextual(LocalDateTime::class, LocalDateTimeSerializer)
            }
        }
        val listOfOrders = if (infoFromFile.isBlank()) listOf() else
            json.decodeFromString<List<Order>>(infoFromFile)
        return listOfOrders
    }

    override fun addDishesIntoOrder(uuid: String, dishes: List<OrderDish>): Order? {
        val listOfOrders = getAllOrders().toMutableList()
        val order = listOfOrders.find { x -> x.id == uuid } ?: return null
        listOfOrders.removeIf { x -> x.id == uuid }
        dishes.forEach { x -> order.dishes.addLast(x) }
        listOfOrders.addLast(order)
        storeInfo(listOfOrders)
        return order
    }

    override fun getOrderById(uuid: String): Order? {
        val listOfOrders = getAllOrders()
        return listOfOrders.find { x -> x.id == uuid }
    }

    override fun getReadyForPaymentOrdersOfUser(login: String): List<String> {
        val listOfOrders = getAllOrders()
        return listOfOrders.filter { x -> x.visitorLogin == login && x.status == OrderStatus.Ready }.map { it.id }
    }

    override fun changeStatus(uuid: String, status: OrderStatus) {
        val listOfOrders = getAllOrders().toMutableList()
        val order = listOfOrders.find { x -> x.id == uuid } ?: return
        listOfOrders.removeIf { x -> x.id == uuid }
        order.status = status
        listOfOrders.addLast(order)
        storeInfo(listOfOrders)
    }

}