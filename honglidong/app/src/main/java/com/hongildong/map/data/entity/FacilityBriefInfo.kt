package com.hongildong.map.data.entity

data class FacilityBriefInfo(
    val description: String? = null,
    val id: Int,
    val isBookmarked: Boolean,
    val latitude: Double,
    val longitude: Double,
    val name: String,
    val nodeId: Int,
    val photoList: List<String>,
    val type: String
)

fun FacilityBriefInfo.toNodeInfo(): NodeInfo {
    return NodeInfo(
        latitude = this.latitude,
        longitude = this.longitude,
        name = this.name,
        nodeName = "",
        type = SearchableNodeType.FACILITY.apiName,
        nodeCode = "",
        id = this.id,
        nodeId = this.nodeId,
        images = this.photoList,
        photoList = this.photoList,
        isBookmarked = this.isBookmarked,
        description = this.description,
        image = ""
    )
}