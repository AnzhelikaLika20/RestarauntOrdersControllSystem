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

    override fun containsDish(name: String) : Boolean {
        val infoFromFile = loadInfo()
        val listOfDishes: MutableList<Dish> =
            if (infoFromFile.isBlank()) mutableListOf() else Json.decodeFromString<List<Dish>>(
                infoFromFile
            ).toMutableList()
        return listOfDishes.any { x -> x.name == name }
    }

    override fun dropDish(dish : Dish) {
        /*val infoFromFile = loadInfo()
        val listOfDishes: MutableList<Dish> =
            if (infoFromFile.isBlank()) mutableListOf() else Json.decodeFromString<List<Dish>>(
                infoFromFile
            ).toMutableList()
        listOfDishes.remove(dish)
        val serializedInfo = Json.encodeToString(listOfDishes)
        storeInfo(serializedInfo)*/
    }

    override fun setAmountForDish(dish : Dish, amount : Int) {
        //dishes[dish.name]?.amount = amount
    }

    override fun setPriceForDish(dish : Dish, price : Double) {
        //dishes[dish.name]?.price = price
    }

    override fun setDifficultyForDish(dish : Dish, durationInMinutes : Int) {
        //dishes[dish.name]?.difficulty = durationInMinutes
    }
}