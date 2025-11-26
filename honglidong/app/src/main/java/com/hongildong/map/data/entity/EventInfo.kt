package com.hongildong.map.data.entity

// 이벤트 전체 조회에 쓸 dto
data class EventInfo(
    val eventEnd: String,
    val eventStart: String,
    val id: Int,
    val image: String,
    val isEventOpen: Boolean,
    val latitude: Int,
    val location: String,
    val longitude: Int,
    val name: String
)