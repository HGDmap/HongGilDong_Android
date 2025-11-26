package com.hongildong.map.data.entity

data class RecommendFacilityInfo(
    val id: Int,
    val images: List<String?>,
    val latitude: Double,
    val location: String,
    val longitude: Double,
    val name: String,
    val nodeId: Int,
    val type: String,
    val isBookmarked: Boolean? = null,
)