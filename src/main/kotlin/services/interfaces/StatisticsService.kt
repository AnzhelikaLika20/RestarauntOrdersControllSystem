package services.interfaces

interface StatisticsService {
    fun getRevenue()
    fun getPopularDishes()
    fun getAverageRatingOfDishes()
    fun getOrderCountOverPeriod()
}