package services

import data.interfaces.ReviewRepository
import data.models.Review
import services.interfaces.ReviewService
import services.models.ResponseCode
import services.models.ReviewResponse
import java.util.*

class ReviewServiceImpl(private val reviewRepository: ReviewRepository) : ReviewService {
    override fun leaveReview(login: String, dishName: String, rating: Int, comment: String): ReviewResponse {
        if (rating < 1 || rating > 5) {
            return ReviewResponse(ResponseCode.BadRequest, "Rating should be in the range from 1 to 5", null)
        }
        val uuid = UUID.randomUUID().toString()
        val review = Review(uuid, login, dishName, rating, comment)
        reviewRepository.addReview(review)
        return ReviewResponse(ResponseCode.Success, "Your review has been recorded\n", review)
    }

    override fun getDishReviews(): List<Review> {
        return reviewRepository.getAllReviews()
    }
}