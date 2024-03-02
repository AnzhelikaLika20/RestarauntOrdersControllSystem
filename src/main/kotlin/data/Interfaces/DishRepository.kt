package data.Interfaces

import data.models.Dish

interface DishRepository {
    fun storeInfo(info: String)
    fun loadInfo() : String
    fun addDish(dish : Dish)
    fun containsDish(name : String) : Boolean
    fun dropDish(dish : Dish)
    fun setAmountForDish(dish : Dish, amount : Int)
    fun setPriceForDish(dish : Dish, price : Double)
    fun setDifficultyForDish(dish : Dish, durationInMinutes : Int)
}