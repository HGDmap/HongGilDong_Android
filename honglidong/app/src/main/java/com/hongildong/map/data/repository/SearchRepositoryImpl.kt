package com.hongildong.map.data.repository

import com.hongildong.map.data.entity.AutoCompleteSearchKeyword
import com.hongildong.map.data.entity.NodeInfo
import com.hongildong.map.data.remote.api.SearchService
import com.hongildong.map.data.util.DefaultResponse
import com.hongildong.map.data.util.safeApiCall
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val api: SearchService
) : SearchRepository {
    override suspend fun searchWithId(
        accessToken: String,
        nodeId: Long
    ): DefaultResponse<NodeInfo> {
        return safeApiCall { api.searchWithId(accessToken, nodeId) }
    }

    override suspend fun autoCompleteSearch(
        keyword: String
    ): DefaultResponse<List<AutoCompleteSearchKeyword>> {
        return safeApiCall { api.autoCompleteSearch(keyword) }
    }
}