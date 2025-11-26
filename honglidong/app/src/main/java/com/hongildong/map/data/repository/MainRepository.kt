package com.hongildong.map.data.repository

import com.hongildong.map.data.remote.response.RecommendPlaceResponse
import com.hongildong.map.data.util.DefaultResponse

interface MainRepository {
    suspend fun getRecommendPlaces(): DefaultResponse<List<RecommendPlaceResponse>>
}