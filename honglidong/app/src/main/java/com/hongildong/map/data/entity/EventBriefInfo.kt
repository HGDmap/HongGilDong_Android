package com.hongildong.map.data.entity

data class EventBriefInfo(
    val eventEnd: String,
    val eventStart: String,
    val id: Int,
    val image: String,
    val isEventOpen: Boolean,
    val latitude: Double,
    val location: String,
    val longitude: Double,
    val name: String
)

fun EventBriefInfo.toNodeInfo(): NodeInfo {
    return NodeInfo(
        latitude = this.latitude,
        longitude = this.longitude,
        name = this.name,
        nodeName = this.name,
        type = SearchableNodeType.EVENT.apiName,
        nodeCode = "",
        id = this.id,
        nodeId = 0,
        images = emptyList(),
        photoList = emptyList(),
        isBookmarked = false,
        description = "",
        image = ""
    )
}