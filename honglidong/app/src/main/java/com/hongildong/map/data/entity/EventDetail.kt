package com.hongildong.map.data.entity

import com.squareup.moshi.Json

data class EventDetail(
    val callNumber: String? = null,
    val eventEnd: String,
    val eventStart: String,
    val isEventOpen: Boolean? = null,
    val homepage: String? = null
)