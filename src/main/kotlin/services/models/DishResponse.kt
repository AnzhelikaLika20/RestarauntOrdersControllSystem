package services.models

import data.models.Dish

data class DishResponse (val status : Int, val hint : String, val dish : Dish?)