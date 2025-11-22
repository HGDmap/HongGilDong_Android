package com.hongildong.map.data.entity

data class FloorInfo(
    val floor: String,
    val facilities: List<FloorFacility>
)

data class FloorFacility(
    val id: Int,
    val name: String,
)
