package com.hongildong.map.data.entity

data class EventLocationInfo(
    val buildingName: String,
    val images: List<String>,
    val isEventOpen: Boolean,
    val nodeId: Int
)