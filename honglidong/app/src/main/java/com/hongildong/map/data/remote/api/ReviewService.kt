package com.hongildong.map.data.remote.api

import com.hongildong.map.data.entity.ReviewInfo
import com.hongildong.map.data.remote.request.ReviewUpdateRequest
import com.hongildong.map.data.util.ApiResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.PUT
import retrofit2.http.Path

interface ReviewService {

    // 특정한 하나의 리뷰 조회
    @GET("review/{reviewId}")
    suspend fun getOneReview(
        @Header("Authorization") accessToken: String,
        @Path("reviewId") reviewId: Int,
    ): ApiResponse<ReviewInfo>

    // 특정한 하나의 리뷰 좋아요 등록 및 취소
    @PUT("review/{reviewId}")
    suspend fun saveReview(
        @Header("Authorization") accessToken: String,
        @Path("reviewId") reviewId: Int,
    ): ApiResponse<ReviewInfo>

    // 특정 리뷰 삭제
    @DELETE("review/{reviewId}")
    suspend fun deleteReview(
        @Header("Authorization") accessToken: String,
        @Path("reviewId") reviewId: Int,
    ): ApiResponse<String>

    // 특정 리뷰 수정
    @PATCH("review/{reviewId}")
    suspend fun updateReview(
        @Header("Authorization") accessToken: String,
        @Path("reviewId") reviewId: Int,
        @Body body: ReviewUpdateRequest
    ): ApiResponse<ReviewInfo>

    // 리뷰 생성
    @PATCH("review/{reviewId}")
    suspend fun createReview(
        @Header("Authorization") accessToken: String,
        @Path("reviewId") reviewId: Int,
        @Body body: ReviewUpdateRequest
    ): ApiResponse<ReviewInfo>
}