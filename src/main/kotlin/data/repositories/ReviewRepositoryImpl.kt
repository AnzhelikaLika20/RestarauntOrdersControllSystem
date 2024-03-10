package data.repositories

import data.Interfaces.ReviewRepository
import data.models.Dish
import data.models.Order
import data.models.Review
import data.models.UserAccount
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import services.utils.LocalTimeSerializer
import java.io.File
import java.io.FileNotFoundException
import java.time.LocalTime

class ReviewRepositoryImpl(private val pathToSerializedStorage: String) : ReviewRepository {
    private val json = Json{prettyPrint = true}

    private fun storeInfo(listOfReviews: List<Review>) {
        val file = File(pathToSerializedStorage)
        val serializedInfo = json.encodeToString(listOfReviews)
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

    override fun addReview(review: Review) {
        val listOfReviews = getAllReviews().toMutableList()
        listOfReviews.removeIf { x -> x.id == review.id }
        listOfReviews.addLast(review)
        storeInfo(listOfReviews)
    }

    override fun getAllReviews() : List<Review> {
        val infoFromFile = loadInfo()
        val listOfReviews: List<Review> =
            if (infoFromFile.isBlank()) listOf() else Json.decodeFromString<List<Review>>(
                infoFromFile
            )
        return listOfReviews
    }
}