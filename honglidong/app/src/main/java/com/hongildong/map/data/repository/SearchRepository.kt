package com.hongildong.map.data.repository

import com.hongildong.map.data.entity.AutoCompleteSearchKeyword
import com.hongildong.map.data.entity.NodeInfo
import com.hongildong.map.data.remote.response.DirectionResponse
import com.hongildong.map.data.remote.response.RawSearchResponse
import com.hongildong.map.data.util.DefaultResponse

interface SearchRepository {
    suspend fun searchWithId(accessToken: String, nodeId: Long): DefaultResponse<NodeInfo>
    suspend fun searchRawWord(query: String): DefaultResponse<RawSearchResponse>
    suspend fun autoCompleteSearch(keyword: String): DefaultResponse<List<AutoCompleteSearchKeyword>>

    suspend fun direct(from: Int, to: Int): DefaultResponse<DirectionResponse>
}