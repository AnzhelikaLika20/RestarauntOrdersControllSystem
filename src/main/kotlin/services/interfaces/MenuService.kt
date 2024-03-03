package services.interfaces

import data.models.Dish
import services.models.AdminResponse
import services.models.AuthResponse

interface MenuService {
    fun addDish(dish : Dish) : AdminResponse
    fun updateDish(dish : Dish) : AdminResponse
    fun dropDish(dishName : String) : AdminResponse
    fun setAmountOfDish(dishName : String, amount : Int) : AdminResponse
    fun setPriceOfDish(dishName : String, price : Double) : AdminResponse
    fun setDifficultyOfDish(dishName : String, difficulty : Int) : AdminResponse
}