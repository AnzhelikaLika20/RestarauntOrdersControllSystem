package data.models

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import services.utils.LocalTimeSerializer
import java.time.Instant
import java.time.LocalTime

@Serializable
data class Order(
    val id: String,
    val dishes: List<OrderDish>,
    var status: OrderStatus,
    var totalPrice: Double,
    var preparingDuration: Int,
    @Serializable(with = LocalTimeSerializer::class)
    val creationTime: LocalTime,
    val visitorLogin: String
)
