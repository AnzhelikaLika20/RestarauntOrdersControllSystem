package services.interfaces

import data.models.Dish
import services.models.DishResponse

interface MenuService {
    fun addDish(dish : Dish) : DishResponse
    fun updateDish(dish : Dish) : DishResponse
    fun dropDish(dishName : String) : DishResponse
    fun setAmountOfDish(dishName : String, amount : Int) : DishResponse
    fun setPriceOfDish(dishName : String, price : Double) : DishResponse
    fun setDifficultyOfDish(dishName : String, difficulty : Int) : DishResponse
    fun getAvailableDishes() : List<Dish>
    fun getDishByName(dishName : String) : DishResponse
}