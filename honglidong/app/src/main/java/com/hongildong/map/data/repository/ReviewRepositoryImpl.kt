package com.hongildong.map.data.repository

import com.hongildong.map.data.entity.ReviewInfo
import com.hongildong.map.data.remote.api.ReviewService
import com.hongildong.map.data.remote.request.ImageUploadRequest
import com.hongildong.map.data.remote.request.ReviewUpdateRequest
import com.hongildong.map.data.remote.response.ImageUploadResponse
import com.hongildong.map.data.util.DefaultResponse
import com.hongildong.map.data.util.safeApiCall
import javax.inject.Inject

class ReviewRepositoryImpl @Inject constructor(
    private val api: ReviewService
): ReviewRepository {

    override suspend fun createPresignedUrl(
        accessToken: String,
        body: ImageUploadRequest
    ): DefaultResponse<List<ImageUploadResponse>> {
        return safeApiCall { api.createPresignedUrl(accessToken, body) }
    }

    override suspend fun getOneReview(
        accessToken: String,
        reviewId: Int
    ): DefaultResponse<ReviewInfo> {
        return safeApiCall { api.getOneReview(accessToken, reviewId) }
    }

    override suspend fun saveReview(
        accessToken: String,
        reviewId: Int
    ): DefaultResponse<ReviewInfo> {
        return safeApiCall { api.saveReview(accessToken, reviewId) }
    }

    override suspend fun deleteReview(
        accessToken: String,
        reviewId: Int
    ): DefaultResponse<String> {
        return safeApiCall { api.deleteReview(accessToken, reviewId) }
    }

    override suspend fun updateReview(
        accessToken: String,
        reviewId: Int,
        body: ReviewUpdateRequest
    ): DefaultResponse<ReviewInfo> {
        return safeApiCall { api.updateReview(accessToken, reviewId, body) }
    }

    override suspend fun createReview(
        accessToken: String,
        reviewId: Int,
        body: ReviewUpdateRequest
    ): DefaultResponse<ReviewInfo> {
        return safeApiCall { api.createReview(accessToken, reviewId, body) }
    }

}