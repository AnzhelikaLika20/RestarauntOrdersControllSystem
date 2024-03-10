package services.interfaces

import services.models.Response
import java.time.LocalDateTime

interface StatisticsService {
    fun getRevenue(): Response
    fun getPopularDishes(): Response
    fun getAverageRatingOfDishes(): Response
    fun getOrderCountOverPeriod(startDateTime: LocalDateTime, endDateTime: LocalDateTime): Response
}