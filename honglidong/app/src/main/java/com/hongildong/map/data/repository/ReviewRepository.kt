package com.hongildong.map.data.repository

import com.hongildong.map.data.entity.ReviewInfo
import com.hongildong.map.data.remote.request.ReviewUpdateRequest
import com.hongildong.map.data.util.DefaultResponse

interface ReviewRepository {
    // 특정한 하나의 리뷰 조회
    suspend fun getOneReview(
        accessToken: String,
        reviewId: Int,
    ): DefaultResponse<ReviewInfo>

    // 특정한 하나의 리뷰 좋아요 등록 및 취소
    suspend fun saveReview(
        accessToken: String,
        reviewId: Int,
    ): DefaultResponse<ReviewInfo>

    // 특정 리뷰 삭제
    suspend fun deleteReview(
        accessToken: String,
        reviewId: Int,
    ): DefaultResponse<String>

    // 특정 리뷰 수정
    suspend fun updateReview(
        accessToken: String,
        reviewId: Int,
        body: ReviewUpdateRequest
    ): DefaultResponse<ReviewInfo>

    // 리뷰 생성
    suspend fun createReview(
        accessToken: String,
        reviewId: Int,
        body: ReviewUpdateRequest
    ): DefaultResponse<ReviewInfo>
}