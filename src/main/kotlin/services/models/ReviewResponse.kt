package services.models

import data.models.Review

class ReviewResponse(status: ResponseCode, hint: String, val review : Review?): Response(status, hint)