package services.interfaces

import data.models.Dish
import services.models.AdminResponse
import services.models.AuthResponse

interface MenuService {
    fun addDish(dish : Dish) : AdminResponse
    fun updateDish(dish : Dish) : AdminResponse
    fun dropDish() : AdminResponse
    fun setAmountOfDish() : AdminResponse
    fun setPriceOfDish() : AdminResponse
    fun setDifficultyOfDish() : AdminResponse
}