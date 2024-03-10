package data.models

import kotlinx.serialization.Serializable
import services.utils.LocalDateTimeSerializer
import java.time.LocalDateTime

@Serializable
data class Order(
    val id: String,
    val dishes: List<OrderDish>,
    var status: OrderStatus,
    var totalPrice: Double,
    var preparingDurationInMinutes: Int,
    @Serializable(with = LocalDateTimeSerializer::class)
    val creationTime: LocalDateTime,
    val visitorLogin: String
)
