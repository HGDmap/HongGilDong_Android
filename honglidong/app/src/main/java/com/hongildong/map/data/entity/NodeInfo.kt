package com.hongildong.map.data.entity

data class NodeInfo(
    val latitude: Double,
    val longitude: Double,
    val name: String? = "",
    val nodeName: String? = "",
    val nodeCode: String,
    val nodeId: Int,
    val image: String? = "",
)