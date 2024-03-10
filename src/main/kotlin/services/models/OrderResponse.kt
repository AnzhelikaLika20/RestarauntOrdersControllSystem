package services.models

import data.models.Order

class OrderResponse(status : ResponseCode, hint : String, val order : Order?) : Response(status, hint)
