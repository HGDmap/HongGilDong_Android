package com.hongildong.map.data.entity

data class NodeInfo(
    val latitude: Double,
    val longitude: Double,
    val name: String? = null,
    val nodeName: String? = null,
    val nodeCode: String? = null,
    val id: Int? = 0, // 시설, 건물, 이벤트 id
    val nodeId: Int, // 연결된 노드 id (길찾기용)
    val images: List<String> = listOf(),
    val photoList: List<String> = listOf(),
    val isBookmarked: Boolean? = false, // 북마크 되어있는지 여부 - 윤정이가 구현한 다음에 바뀔수잇슴..
    val description: String? = null
)

fun NodeInfo.toSearchKeyword(): SearchKeyword {
    return SearchKeyword(
        nodeName = this.nodeName ?: this.name ?: "temp name",
        id = this.id ?: 0,
        nodeId = this.nodeId,
        nodeCode = this.nodeCode ?: "temp code",
    )
}