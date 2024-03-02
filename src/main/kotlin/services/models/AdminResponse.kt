package services.models

import data.models.Dish

data class AdminResponse (val status : Int, val hint : String, val dish : Dish?)