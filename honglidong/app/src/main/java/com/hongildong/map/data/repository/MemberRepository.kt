package com.hongildong.map.data.repository

import com.hongildong.map.data.remote.request.ImageUploadRequest
import com.hongildong.map.data.remote.request.ProfileUpdateRequest
import com.hongildong.map.data.remote.response.ImageUploadResponse
import com.hongildong.map.data.remote.response.ProfileResponse
import com.hongildong.map.data.remote.response.ReviewResponse
import com.hongildong.map.data.util.ApiResponse
import com.hongildong.map.data.util.DefaultResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Query

interface MemberRepository {
    // 프로필 사진 수정을 위한 s3 링크 받기
    suspend fun createPresignedUrl(
        accessToken: String,
        body: ImageUploadRequest
    ): DefaultResponse<List<ImageUploadResponse>>

    // 회원 프로필 수정
    suspend fun updateProfile(
        accessToken: String,
        body: ProfileUpdateRequest
    ): DefaultResponse<ProfileResponse>

    // 회원 정보 조회
    suspend fun getProfile(
        accessToken: String,
    ): DefaultResponse<ProfileResponse>

    // 내가 쓴 리뷰 조회
    suspend fun getMyReviews(
        accessToken: String,
        page: Int,
        size: Int,
    ): DefaultResponse<ReviewResponse>

    // 좋아요한 리뷰 조회
    suspend fun getLikeReviews(
        accessToken: String,
        page: Int,
        size: Int,
    ): DefaultResponse<ReviewResponse>
}