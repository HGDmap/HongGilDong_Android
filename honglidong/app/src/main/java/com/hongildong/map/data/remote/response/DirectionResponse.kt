package com.hongildong.map.data.remote.response

import com.hongildong.map.data.entity.NodeInfo

data class DirectionResponse(
    val minute: Int,
    val nodes: List<NodeInfo>,
    val seconds: Int
)