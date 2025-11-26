package com.hongildong.map.data.repository

import com.hongildong.map.data.remote.response.RecommendPlace
import com.hongildong.map.data.remote.response.RecommendPlaceResponse
import com.hongildong.map.data.util.DefaultResponse

interface MainRepository {
    suspend fun getRecommendPlaces(accessToken: String?): DefaultResponse<RecommendPlaceResponse>
}