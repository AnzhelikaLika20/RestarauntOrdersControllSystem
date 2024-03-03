package services.models

import data.models.Dish

data class DishResponse (val status : ResponseCode, val hint : String, val dish : Dish?)