package com.hongildong.map.data.repository

import com.hongildong.map.data.entity.ReviewInfo
import com.hongildong.map.data.remote.request.ReviewImageUploadRequest
import com.hongildong.map.data.remote.request.ReviewUpdateRequest
import com.hongildong.map.data.remote.response.ImageUploadResponse
import com.hongildong.map.data.util.DefaultResponse

interface ReviewRepository {
    // 시설 리뷰 등록시 사진 등록 - s3 업로드용 링크 받기
    // s3 사용법
    // 1. 리뷰에서 사진 등록할 경우 링크 받기 api로 파일명 리스트를 보내서 링크를 받음
    // 2. 해당 api에서 받은 presignedURL로 Put 요청 보내기
    // 3. 해당 api에서 받은 imageURL은 리뷰 등록 api로 보내기
    suspend fun createPresignedUrl(
        accessToken: String,
        body: ReviewImageUploadRequest
    ): DefaultResponse<List<ImageUploadResponse>>

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