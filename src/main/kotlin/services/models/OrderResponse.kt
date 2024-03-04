package services.models

import data.models.Order

data class OrderResponse(val status : ResponseCode, val hint : String, val dish : Order?)
