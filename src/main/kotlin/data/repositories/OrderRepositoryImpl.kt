package data.repositories

import data.Interfaces.OrderRepository
import data.models.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import services.utils.LocalTimeSerializer
import java.io.File
import java.io.FileNotFoundException
import java.time.LocalTime

class OrderRepositoryImpl(private val pathToSerializedStorage: String) : OrderRepository {
    override fun storeInfo(info: String) {
        val file = File(pathToSerializedStorage)
        file.writeText(info)
    }

    override fun loadInfo(): String {
        val file = File(pathToSerializedStorage)
        try {
            return file.readText()
        } catch (ex: FileNotFoundException) {
            file.createNewFile()
            return ""
        }
    }
    override fun addOrder(order: Order) {
        val infoFromFile = loadInfo()
        val json = Json {
            serializersModule = SerializersModule {
                contextual(LocalTime::class, LocalTimeSerializer)
            }
        }
        val listOfOrders = if (infoFromFile.isBlank()) mutableListOf() else
            json.decodeFromString<List<Order>>(infoFromFile).toMutableList()
        listOfOrders.addLast(order)
        val serializedInfo = Json.encodeToString(listOfOrders)
        storeInfo(serializedInfo)
    }

    override fun containsOrder(uuid: String): Boolean {
        val infoFromFile = loadInfo()
        val json = Json {
            serializersModule = SerializersModule {
                contextual(LocalTime::class, LocalTimeSerializer)
            }
        }
        val listOfOrders = if (infoFromFile.isBlank()) mutableListOf() else
            json.decodeFromString<List<Order>>(infoFromFile).toMutableList()
        return listOfOrders.any{x -> x.id == uuid}
    }

    override fun getActiveOrdersOfUser(login: String): List<String> {
        val infoFromFile = loadInfo()
        val json = Json {
            serializersModule = SerializersModule {
                contextual(LocalTime::class, LocalTimeSerializer)
            }
        }
        val listOfOrders = if (infoFromFile.isBlank()) mutableListOf() else
            json.decodeFromString<List<Order>>(infoFromFile).toMutableList()
        return listOfOrders.filter { x -> x.visitorLogin == login && x.status == OrderStatus.Preparing}.map{it.id}
    }

    override fun addDishIntoOrder(uuid: String, dishes : List<OrderDish>) : Order? {
        val infoFromFile = loadInfo()
        val json = Json {
            serializersModule = SerializersModule {
                contextual(LocalTime::class, LocalTimeSerializer)
            }
        }
        val listOfOrders = if (infoFromFile.isBlank()) mutableListOf() else
            json.decodeFromString<List<Order>>(infoFromFile).toMutableList()
        val order = listOfOrders.find { x -> x.id == uuid } ?: return null
        listOfOrders.removeIf { x -> x.id == uuid }
        dishes.forEach { x -> order.dishes.addLast(x) }
        val serializedInfo = Json.encodeToString(listOfOrders)
        storeInfo(serializedInfo)
        addOrder(order)
        return order
    }
}