package com.hongildong.map.data.remote.response

import com.hongildong.map.data.entity.NodeInfo

data class RawSearchResponse (
    val listSize: Int,
    val resultList: List<NodeInfo>
)