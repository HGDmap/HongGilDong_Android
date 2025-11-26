package com.hongildong.map.data.repository

import com.hongildong.map.data.remote.api.MainService
import com.hongildong.map.data.remote.response.RecommendPlaceResponse
import com.hongildong.map.data.util.DefaultResponse
import com.hongildong.map.data.util.safeApiCall
import jakarta.inject.Inject

class MainRepositoryImpl @Inject constructor(
    private val api: MainService
): MainRepository {
    override suspend fun getRecommendPlaces(): DefaultResponse<List<RecommendPlaceResponse>> {
        return safeApiCall { api.getRecommendPlaces() }
    }

}