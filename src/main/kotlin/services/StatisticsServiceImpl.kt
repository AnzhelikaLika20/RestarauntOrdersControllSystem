package services

import data.interfaces.OrderRepository
import data.interfaces.ReviewRepository
import data.interfaces.StatisticsRepository
import services.interfaces.StatisticsService
import services.models.Response
import services.models.ResponseCode
import java.time.LocalDateTime

class StatisticsServiceImpl(
    private val statisticsRepository: StatisticsRepository,
    private val orderRepository: OrderRepository,
    private val reviewRepository: ReviewRepository
) : StatisticsService {
    override fun getRevenue(): Response {
        val revenue = statisticsRepository.getStatistics()
        return Response(ResponseCode.Success, "Restaurant's revenue: ${revenue.revenue}")
    }

    override fun getPopularDishes(): Response {
        val orders = orderRepository.getAllOrders()
        if (orders.isEmpty())
            return Response(ResponseCode.Success, "No information about orders found. Cannot show popular dishes\n")
        val dishCounts = orders
            .flatMap { it.dishes }
            .groupingBy { it.name }
            .eachCount()
            .toList()
            .sortedByDescending { it.second }
            .take(5)
        return Response(ResponseCode.Success,
            dishCounts.joinToString(
                prefix = "Top 5 popular dishes: \n\t",
                separator = "\n\t"
            ) { "Dish: \"${it.first}\": have bought ${it.second} times" })
    }

    override fun getAverageRatingOfDishes(): Response {
        val reviews = reviewRepository.getAllReviews()
        if (reviews.isEmpty())
            return Response(ResponseCode.Success, "There are no any reviews in the system\n")
        return Response(ResponseCode.Success, "Average rating of dishes: ${reviews.map { it.rate }.average()}")
    }

    override fun getOrderCountOverPeriod(startDateTime: LocalDateTime, endDateTime: LocalDateTime): Response {
        val orders = orderRepository.getAllOrders()
        if (orders.isEmpty())
            return Response(ResponseCode.Success, "There are no any orders in system\n")

        return Response(ResponseCode.Success,
            "The number of orders made in chosen period: ${
                orders.count {
                    it.creationTime.isAfter(startDateTime) && it.creationTime.isBefore(endDateTime)
                }
            }"
        )
    }
}