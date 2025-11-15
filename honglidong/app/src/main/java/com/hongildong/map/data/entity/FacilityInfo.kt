package com.hongildong.map.data.entity

data class FacilityInfo(
    val description: String,
    val name: String,
    val id: Int, // 시설의 id
    val isBookmarked: Boolean,
    val latitude: Double,
    val link: String? = null,
    val longitude: Double,
    val nodeId: Int, // 연결된 node의 id
    val nodeName: String,
    val open: String? = null,
    val phone: String,
    val photoList: List<String>,
    val type: String
)

fun FacilityInfo.toSearchKeyword(): SearchKeyword {
    return SearchKeyword(
        nodeName = this.name,
        id = this.id,
        nodeId = this.nodeId,
        nodeCode = this.type,
    )
}