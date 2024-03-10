package data.models

import kotlinx.serialization.Serializable

@Serializable
data class Review(val id : String, val login : String, val dishName : String, val rate : Int, var comment : String)
