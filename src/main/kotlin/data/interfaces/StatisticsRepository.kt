package data.interfaces

import data.models.Statistics

interface StatisticsRepository {
    fun getStatistics(): Statistics
    fun saveStatistics(statistics: Statistics)
}