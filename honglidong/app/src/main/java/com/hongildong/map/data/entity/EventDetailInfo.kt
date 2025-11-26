package com.hongildong.map.data.entity

// 이벤트 상세 정보
data class EventDetailInfo(
    val eventInfo: EventDetail,
    val id: Int,
    val image: String,
    val location: String,
    val locationInfo: EventLocationInfo,
    val name: String
)