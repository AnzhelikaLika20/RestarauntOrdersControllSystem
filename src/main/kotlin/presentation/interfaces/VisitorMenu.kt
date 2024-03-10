package presentation.interfaces

import data.models.VisitorAccount
import services.models.OrderResponse
import services.models.Response
import services.models.ReviewResponse

interface VisitorMenu {
    fun displayMenuOptions()
    fun dealWithUser(account: VisitorAccount): Response
    fun createOrder(account: VisitorAccount): OrderResponse
    fun addDishIntoOrder(account: VisitorAccount): OrderResponse
    fun payOrder(account: VisitorAccount): OrderResponse
    fun createReview(account: VisitorAccount): ReviewResponse
    fun exit(): OrderResponse
}