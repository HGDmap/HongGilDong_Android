package com.hongildong.map.data.repository

import com.hongildong.map.data.entity.NodeInfo
import com.hongildong.map.data.util.DefaultResponse

interface SearchRepository {
    suspend fun searchWithId(accessToken: String, nodeId: Long): DefaultResponse<NodeInfo>
}