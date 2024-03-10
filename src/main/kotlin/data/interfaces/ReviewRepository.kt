package data.interfaces

import data.models.Review

interface ReviewRepository {
    fun addReview(review: Review)
    fun getAllReviews(): List<Review>
}