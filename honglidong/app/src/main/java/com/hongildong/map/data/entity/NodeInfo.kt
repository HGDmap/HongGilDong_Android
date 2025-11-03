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
    val isBookmarked: Boolean = false // 북마크 되어있는지 여부 - 윤정이가 구현한 다음에 바뀔수잇슴..
)