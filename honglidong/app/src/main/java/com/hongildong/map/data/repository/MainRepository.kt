package com.hongildong.map.data.repository

import com.hongildong.map.data.remote.response.AllEventResponse
import com.hongildong.map.data.remote.response.FacilityTypeResponse
import com.hongildong.map.data.remote.response.RecommendPlaceResponse
import com.hongildong.map.data.util.DefaultResponse

interface MainRepository {
    suspend fun getRecommendPlaces(accessToken: String?): DefaultResponse<RecommendPlaceResponse>

    suspend fun getAllEvents(): DefaultResponse<AllEventResponse>

    suspend fun getFacilityByType(accessToken: String?, type: String): DefaultResponse<FacilityTypeResponse>
}