package com.hongildong.map.data.remote.api

import com.hongildong.map.data.remote.response.RecommendPlace
import com.hongildong.map.data.remote.response.RecommendPlaceResponse
import com.hongildong.map.data.util.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Header

interface MainService {
    // 여기는 어때요 - 장소 추천
    @GET("recommend/places")
    suspend fun getRecommendPlaces(
        @Header("Authorization") accessToken: String?,
    ): ApiResponse<RecommendPlaceResponse>

    // 키워드 검색
}