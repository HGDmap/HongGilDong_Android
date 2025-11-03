package com.hongildong.map.data.entity

data class NodeInfo(
    val latitude: Double,
    val longitude: Double,
    val name: String? = "",
    val nodeName: String? = "",
    val nodeCode: String,
    val id: Int? = 0, // 시설, 건물, 이벤트 id
    val nodeId: Int, // 연결된 노드 id (길찾기용)
    val image: String? = "",
)