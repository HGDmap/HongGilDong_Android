package com.hongildong.map.data.remote.api

import com.hongildong.map.data.entity.AutoCompleteSearchKeyword
import com.hongildong.map.data.entity.NodeInfo
import com.hongildong.map.data.remote.response.DirectionResponse
import com.hongildong.map.data.util.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface SearchService {
    // 임시 검색 로직 - 나중에 facility id로 검색하는 api 연결 예정
    @GET("node/{nodeId}")
    suspend fun searchWithId(
        @Header("Authorization") accessToken: String,
        @Path("nodeId") nodeId: Long
    ): ApiResponse<NodeInfo>

    @GET("search/{query}")
    suspend fun searchRawWord(
        @Path("query") query: String
    ): ApiResponse<List<NodeInfo>>

    // 검색 자동완성 api
    @GET("search/list/{keyword}")
    suspend fun autoCompleteSearch(
        @Path("keyword") keyword: String
    ): ApiResponse<List<AutoCompleteSearchKeyword>>


    // 길찾기 api
    @GET("directions")
    suspend fun direct(
        @Query("from") from: Int,
        @Query("to") to: Int
    ): ApiResponse<DirectionResponse>
}