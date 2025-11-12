package com.hongildong.map.data.entity

data class FacilityInfo(
    val description: String,
    val id: Int, // 시설의 id
    val isBookmarked: Boolean,
    val latitude: Int,
    val link: String,
    val longitude: Int,
    val nodeId: Int, // 연결된 node의 id
    val nodeName: String,
    val open: String,
    val phone: String,
    val photoList: List<String>,
    val type: String
)