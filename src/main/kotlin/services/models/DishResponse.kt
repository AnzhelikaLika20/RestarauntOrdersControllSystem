package services.models

import data.models.Dish

class DishResponse(status: ResponseCode, hint: String, val dish: Dish?) : Response(status, hint)
