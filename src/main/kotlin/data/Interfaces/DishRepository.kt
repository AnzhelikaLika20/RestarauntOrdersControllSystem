package data.Interfaces

import data.models.Dish

interface DishRepository {
    fun storeInfo(info: String)
    fun loadInfo() : String
    fun addDish(dish : Dish)
    fun containsDish(dishName : String) : Boolean
    fun dropDish(dishName : String)
    fun setAmountForDish(dishName : String, amount : Int)
    fun setPriceForDish(dishName : String, price : Double)
    fun setDifficultyForDish(dishName : String, durationInMinutes : Int)
}