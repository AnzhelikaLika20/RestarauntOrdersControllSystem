package services.interfaces

import data.models.Review
import services.models.ReviewResponse

interface ReviewService {
    fun leaveReview(login: String, dishName: String, rating: Int, comment: String): ReviewResponse
    fun getDishReviews(): List<Review>
}