package com.hongildong.map.data.remote.request

data class ReviewUpdateRequest(
    val content: String,
    val photoList: List<String>,
    val rating: Float,
    val recommend: String, // FacilityRecommendType의 apiName으로 주기
)