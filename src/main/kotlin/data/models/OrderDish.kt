package data.models

import kotlinx.serialization.Serializable

@Serializable
data class OrderDish(val name: String, var price: Double, var difficulty: Int)
