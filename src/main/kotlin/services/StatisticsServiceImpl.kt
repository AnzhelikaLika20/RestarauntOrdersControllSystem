package services

import data.repositories.ReviewRepositoryImpl
import data.repositories.StatisticsRepositoryImpl
import services.interfaces.StatisticsService

class StatisticsServiceImpl(private val statisticsRepository: StatisticsRepositoryImpl): StatisticsService {
    override fun getRevenue() {
        TODO("Not yet implemented")
    }

    override fun getPopularDishes() {
        TODO("Not yet implemented")
    }

    override fun getAverageRatingOfDishes() {
        TODO("Not yet implemented")
    }

    override fun getOrderCountOverPeriod() {
        TODO("Not yet implemented")
    }
}