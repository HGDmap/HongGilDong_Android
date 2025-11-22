package com.hongildong.map.data.remote.api

import com.hongildong.map.data.remote.request.ImageUploadRequest
import com.hongildong.map.data.remote.request.ProfileUpdateRequest
import com.hongildong.map.data.remote.response.ImageUploadResponse
import com.hongildong.map.data.remote.response.ProfileResponse
import com.hongildong.map.data.remote.response.ReviewResponse
import com.hongildong.map.data.util.ApiResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Query

interface MemberService {
    // 프로필 사진 수정을 위한 s3 링크 받기
    @POST("image/presigned-url/MEMBER")
    suspend fun createPresignedUrl(
        @Header("Authorization") accessToken: String,
        @Body body: ImageUploadRequest
    ): ApiResponse<List<ImageUploadResponse>>

    // 회원 프로필 수정
    @PATCH("member/mypage/profile")
    suspend fun updateProfile(
        @Header("Authorization") accessToken: String,
        @Body body: ProfileUpdateRequest
    ): ApiResponse<ProfileResponse>

    // 회원 정보 조회
    @GET("member/profile")
    suspend fun getProfile(
        @Header("Authorization") accessToken: String,
    ): ApiResponse<ProfileResponse>

    // 내가 쓴 리뷰 조회
    @GET("member/mypage/review")
    suspend fun getMyReviews(
        @Header("Authorization") accessToken: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): ApiResponse<ReviewResponse>

    // 좋아요한 리뷰 조회
    @GET("member/mypage/likes")
    suspend fun getLikeReviews(
        @Header("Authorization") accessToken: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): ApiResponse<ReviewResponse>
}