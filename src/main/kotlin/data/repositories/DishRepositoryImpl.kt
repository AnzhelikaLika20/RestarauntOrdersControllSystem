package data.repositories

import data.interfaces.DishRepository
import data.models.Dish
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.io.FileNotFoundException

class DishRepositoryImpl(private val pathToSerializedStorage: String) : DishRepository {
    private val json = Json { prettyPrint = true }
    private fun storeInfo(listOrders: List<Dish>) {
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

    override fun addDish(dish: Dish) {
        val listOfDishes = getAllDishes().toMutableList()
        listOfDishes.removeIf { x -> x.name == dish.name }
        listOfDishes.addLast(dish)
        storeInfo(listOfDishes)
    }

    override fun containsDish(dishName: String): Boolean {
        val listOfDishes = getAllDishes()
        return listOfDishes.any { x -> x.name == dishName }
    }

    override fun dropDish(dishName: String) {
        val listOfDishes = getAllDishes().toMutableList()
        listOfDishes.removeIf { x -> x.name == dishName }
        storeInfo(listOfDishes)
    }

    override fun setAmountForDish(dishName: String, amount: Int) {
        val listOfDishes = getAllDishes()
        val dish: Dish = listOfDishes.find { x -> x.name == dishName } ?: return
        dish.amount = amount
        dropDish(dishName)
        addDish(dish)
        storeInfo(listOfDishes)
    }

    override fun setPriceForDish(dishName: String, price: Double) {
        val listOfDishes = getAllDishes()
        val dish: Dish = listOfDishes.find { x -> x.name == dishName } ?: return
        dish.price = price
        dropDish(dishName)
        addDish(dish)
        storeInfo(listOfDishes)
    }

    override fun setDifficultyForDish(dishName: String, durationInMinutes: Int) {
        val listOfDishes = getAllDishes()
        val dish: Dish = listOfDishes.find { x -> x.name == dishName } ?: return
        dish.difficulty = durationInMinutes
        dropDish(dishName)
        addDish(dish)
        storeInfo(listOfDishes)
    }

    override fun getAvailableDishes(): List<Dish> {
        val listOfDishes = getAllDishes()
        return listOfDishes.filter { x -> x.amount > 0 }
    }

    override fun getDishByName(dishName: String): Dish? {
        val listOfDishes = getAllDishes()
        return listOfDishes.find { x -> x.name == dishName }
    }

    private fun getAllDishes(): List<Dish> {
        val infoFromFile = loadInfo()
        val listOfDishes: List<Dish> =
            if (infoFromFile.isBlank()) listOf() else Json.decodeFromString<List<Dish>>(
                infoFromFile
            )
        return listOfDishes
    }
}