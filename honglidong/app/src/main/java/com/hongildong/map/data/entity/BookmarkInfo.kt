package com.hongildong.map.data.entity

data class BookmarkInfo(
    val id: Int,
    val image: String,
    val latitude: Double,
    val location: String,
    val longitude: Double,
    val name: String,
    val nodeId: Int,
    val type: String
)