package com.hongildong.map.data.remote.api

import com.hongildong.map.data.remote.response.AllEventResponse
import com.hongildong.map.data.remote.response.FacilityTypeResponse
import com.hongildong.map.data.remote.response.RecommendPlaceResponse
import com.hongildong.map.data.util.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface MainService {
    // 여기는 어때요 - 장소 추천
    @GET("recommend/places")
    suspend fun getRecommendPlaces(
        @Header("Authorization") accessToken: String?,
    ): ApiResponse<RecommendPlaceResponse>

    // 이벤트 모아보기
    @GET("events")
    suspend fun getAllEvents(): ApiResponse<AllEventResponse>

    // 시설 타입별 모아보기
    @GET("facility/{type}/collection")
    suspend fun getFacilityByType(
        @Header("Authorization") accessToken: String?,
        @Path("type") type: String,
    ): ApiResponse<FacilityTypeResponse>
}