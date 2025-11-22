package com.hongildong.map.data.repository

import com.hongildong.map.data.remote.api.MemberService
import com.hongildong.map.data.remote.request.ImageUploadRequest
import com.hongildong.map.data.remote.request.ProfileUpdateRequest
import com.hongildong.map.data.remote.response.ImageUploadResponse
import com.hongildong.map.data.remote.response.ProfileResponse
import com.hongildong.map.data.remote.response.ReviewResponse
import com.hongildong.map.data.util.ApiResponse
import com.hongildong.map.data.util.DefaultResponse
import com.hongildong.map.data.util.safeApiCall
import javax.inject.Inject

class MemberRepositoryImpl @Inject constructor(
    private val api: MemberService
): MemberRepository {
    override suspend fun createPresignedUrl(
        accessToken: String,
        body: ImageUploadRequest
    ): DefaultResponse<List<ImageUploadResponse>> {
        return safeApiCall { api.createPresignedUrl(accessToken, body) }
    }

    override suspend fun updateProfile(
        accessToken: String,
        body: ProfileUpdateRequest
    ): DefaultResponse<ProfileResponse> {
        return safeApiCall { api.updateProfile(accessToken, body) }
    }

    override suspend fun getProfile(accessToken: String): DefaultResponse<ProfileResponse> {
        return safeApiCall { api.getProfile(accessToken) }
    }

    override suspend fun getMyReviews(
        accessToken: String,
        page: Int,
        size: Int
    ): DefaultResponse<ReviewResponse> {
        return safeApiCall { api.getMyReviews(accessToken, page, size) }
    }

    override suspend fun getLikeReviews(
        accessToken: String,
        page: Int,
        size: Int
    ): DefaultResponse<ReviewResponse> {
        return safeApiCall { api.getLikeReviews(accessToken, page, size) }
    }
}