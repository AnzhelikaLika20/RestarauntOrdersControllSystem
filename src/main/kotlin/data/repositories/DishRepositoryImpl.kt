package data.repositories

import data.Interfaces.DishRepository
import data.models.Dish
import data.models.UserAccount
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.io.FileNotFoundException

class DishRepositoryImpl(private val pathToSerializedStorage: String) : DishRepository {

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

    override fun addDish(dish : Dish) {
        val infoFromFile = loadInfo()
        val listOfDishes: MutableList<Dish> =
            if (infoFromFile.isBlank()) mutableListOf() else Json.decodeFromString<List<Dish>>(
                infoFromFile
            ).toMutableList()
        listOfDishes.removeIf { x -> x.name == dish.name }
        listOfDishes.addLast(dish)
        val serializedInfo = Json.encodeToString(listOfDishes)
        storeInfo(serializedInfo)
    }

    override fun containsDish(dishName: String) : Boolean {
        val infoFromFile = loadInfo()
        val listOfDishes: MutableList<Dish> =
            if (infoFromFile.isBlank()) mutableListOf() else Json.decodeFromString<List<Dish>>(
                infoFromFile
            ).toMutableList()
        return listOfDishes.any { x -> x.name == dishName }
    }

    override fun dropDish(dishName : String) {
        val infoFromFile = loadInfo()
        val listOfDishes: MutableList<Dish> =
            if (infoFromFile.isBlank()) mutableListOf() else Json.decodeFromString<List<Dish>>(
                infoFromFile
            ).toMutableList()
        listOfDishes.removeIf { x -> x.name == dishName }
        val serializedInfo = Json.encodeToString(listOfDishes)
        storeInfo(serializedInfo)
    }

    override fun setAmountForDish(dishName : String, amount : Int) {
        val infoFromFile = loadInfo()
        val listOfDishes: MutableList<Dish> =
            if (infoFromFile.isBlank()) mutableListOf() else Json.decodeFromString<List<Dish>>(
                infoFromFile
            ).toMutableList()
        val dish : Dish = listOfDishes.find { x -> x.name == dishName } ?: return
        dish.amount = amount
        dropDish(dishName)
        addDish(dish)
        val serializedInfo = Json.encodeToString(listOfDishes)
        storeInfo(serializedInfo)
    }

    override fun setPriceForDish(dishName : String, price : Double) {
        val infoFromFile = loadInfo()
        val listOfDishes: MutableList<Dish> =
            if (infoFromFile.isBlank()) mutableListOf() else Json.decodeFromString<List<Dish>>(
                infoFromFile
            ).toMutableList()
        val dish : Dish = listOfDishes.find { x -> x.name == dishName } ?: return
        dish.price = price
        dropDish(dishName)
        addDish(dish)
        val serializedInfo = Json.encodeToString(listOfDishes)
        storeInfo(serializedInfo)
    }

    override fun setDifficultyForDish(dishName : String, durationInMinutes : Int) {
        val infoFromFile = loadInfo()
        val listOfDishes: MutableList<Dish> =
            if (infoFromFile.isBlank()) mutableListOf() else Json.decodeFromString<List<Dish>>(
                infoFromFile
            ).toMutableList()
        val dish : Dish = listOfDishes.find { x -> x.name == dishName } ?: return
        dish.difficulty = durationInMinutes
        dropDish(dishName)
        addDish(dish)
        val serializedInfo = Json.encodeToString(listOfDishes)
        storeInfo(serializedInfo)
    }

    override fun getAvailableDishes(): List<Dish> {
        val infoFromFile = loadInfo()
        val listOfDishes: MutableList<Dish> =
            if (infoFromFile.isBlank()) mutableListOf() else Json.decodeFromString<List<Dish>>(
                infoFromFile
            ).toMutableList()
        return listOfDishes.filter { x -> x.amount > 0 }
    }

    override fun getDishByName(dishName : String): Dish? {
        val infoFromFile = loadInfo()
        val listOfDishes: MutableList<Dish> =
            if (infoFromFile.isBlank()) mutableListOf() else Json.decodeFromString<List<Dish>>(
                infoFromFile
            ).toMutableList()
        return listOfDishes.find { x -> x.name == dishName }
    }
}