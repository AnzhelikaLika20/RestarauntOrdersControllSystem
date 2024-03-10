package data.repositories

import data.interfaces.StatisticsRepository
import data.models.Statistics
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.io.FileNotFoundException

class StatisticsRepositoryImpl(private val pathToSerializedStorage: String) : StatisticsRepository {
    private val json = Json { prettyPrint = true }
    private fun storeInfo(statistics: String) {
        val file = File(pathToSerializedStorage)
        file.writeText(statistics)
    }

    private fun loadInfo(): String {
        val file = File(pathToSerializedStorage)
        try {
            return file.readText()
        } catch (_: FileNotFoundException) {
            file.createNewFile()
            return ""
        }
    }

    override fun getStatistics(): Statistics {
        val infoFromFile = loadInfo()
        if (infoFromFile.isBlank())
            return Statistics(0.0)
        val statistics: Statistics = json.decodeFromString<Statistics>(
            infoFromFile
        )
        return statistics
    }

    override fun saveStatistics(statistics: Statistics) {
        val serializedStatistics = json.encodeToString(statistics)
        storeInfo(serializedStatistics)
    }
}