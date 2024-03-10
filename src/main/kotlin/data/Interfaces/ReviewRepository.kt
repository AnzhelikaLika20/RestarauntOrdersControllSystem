package data.Interfaces

import data.models.Review
import data.models.UserAccount
import services.models.ReviewResponse

interface ReviewRepository {
    fun addReview(review: Review)
    fun getAllReviews() : List<Review>
}