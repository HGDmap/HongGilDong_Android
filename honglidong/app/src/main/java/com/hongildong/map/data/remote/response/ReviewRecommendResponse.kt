package com.hongildong.map.data.remote.response

import com.hongildong.map.data.remote.response.Recommendation

data class ReviewRecommendResponse(
    val avgRating: Float = 4.5f,
    val recommendation: Recommendation = Recommendation()
)