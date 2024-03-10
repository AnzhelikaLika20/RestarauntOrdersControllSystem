package data.repositories

import data.Interfaces.StatisticsRepository
import data.models.Dish
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.io.FileNotFoundException

class StatisticsRepositoryImpl(private val pathToSerializedStorage: String) : StatisticsRepository {
    private val json = Json{prettyPrint = true}
    private fun storeInfo(listOrders: List<Dish>) {
        val file = File(pathToSerializedStorage)
        val serializedInfo = json.encodeToString(listOrders)
        file.writeText(serializedInfo)
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
    override fun getStatistics() {
        TODO("Not yet implemented")
    }

    override fun saveStatistics() {
        TODO("Not yet implemented")
    }
}