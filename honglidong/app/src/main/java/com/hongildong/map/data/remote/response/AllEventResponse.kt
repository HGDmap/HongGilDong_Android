package com.hongildong.map.data.remote.response

import com.hongildong.map.data.entity.EventBriefInfo

data class AllEventResponse(
    val events: List<EventBriefInfo>
)