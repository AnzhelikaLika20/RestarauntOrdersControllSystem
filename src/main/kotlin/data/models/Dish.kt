package data.models

import kotlinx.serialization.Serializable

@Serializable
class Dish(val name: String, var amount: Int, var price: Double, var difficulty: Int)