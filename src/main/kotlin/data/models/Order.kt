package data.models

import javax.print.attribute.standard.DateTimeAtCreation

data class Order(
    val id: Int,
    val dishes: List<Dish>,
    var status: OrderStatus,
    var totalPrice: Double,
    var preparingDuration: Int,
    val creationTime: DateTimeAtCreation
)
