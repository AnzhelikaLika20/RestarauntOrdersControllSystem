package data.repositories

import data.interfaces.ReviewRepository
import data.models.Review
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.io.FileNotFoundException

class ReviewRepositoryImpl(private val pathToSerializedStorage: String) : ReviewRepository {
    private val json = Json { prettyPrint = true }

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

    override fun getAllReviews(): List<Review> {
        val infoFromFile = loadInfo()
        val listOfReviews: List<Review> =
            if (infoFromFile.isBlank()) listOf() else Json.decodeFromString<List<Review>>(
                infoFromFile
            )
        return listOfReviews
    }
}