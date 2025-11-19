package com.hongildong.map.data.remote.api

import com.hongildong.map.data.entity.ReviewInfo
import com.hongildong.map.data.remote.request.ImageUploadRequest
import com.hongildong.map.data.remote.request.ReviewUpdateRequest
import com.hongildong.map.data.remote.response.ImageUploadResponse
import com.hongildong.map.data.util.ApiResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ReviewService {

    // 시설 리뷰 등록시 사진 등록 - s3 업로드용 링크 받기
    // s3 사용법
    // 1. 리뷰에서 사진 등록할 경우 링크 받기 api로 파일명 리스트를 보내서 링크를 받음
    // 2. 해당 api에서 받은 presignedURL로 Put 요청 보내기
    // 3. 해당 api에서 받은 imageURL은 리뷰 등록 api로 보내기
    @POST("image/presigned-url")
    suspend fun createPresignedUrl(
        @Header("Authorization") accessToken: String,
        @Body body: ImageUploadRequest
    ): ApiResponse<List<ImageUploadResponse>>

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
    @POST("review/{facilityId}")
    suspend fun createReview(
        @Header("Authorization") accessToken: String,
        @Path("facilityId") facilityId: Int,
        @Body body: ReviewUpdateRequest
    ): ApiResponse<ReviewInfo>
}