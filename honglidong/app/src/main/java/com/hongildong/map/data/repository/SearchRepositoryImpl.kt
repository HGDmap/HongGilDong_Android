package com.hongildong.map.data.repository

import com.hongildong.map.data.entity.AutoCompleteSearchKeyword
import com.hongildong.map.data.entity.FacilityInfo
import com.hongildong.map.data.entity.NodeInfo
import com.hongildong.map.data.remote.api.SearchService
import com.hongildong.map.data.remote.request.PhotoRequest
import com.hongildong.map.data.remote.response.DirectionResponse
import com.hongildong.map.data.remote.response.PhotoResponse
import com.hongildong.map.data.remote.response.RawSearchResponse
import com.hongildong.map.data.remote.response.ReviewResponse
import com.hongildong.map.data.util.DefaultResponse
import com.hongildong.map.data.util.safeApiCall
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val api: SearchService
) : SearchRepository {
    override suspend fun searchWithId(
        accessToken: String,
        nodeId: Long
    ): DefaultResponse<NodeInfo> {
        return safeApiCall { api.searchWithId(accessToken, nodeId) }
    }

    override suspend fun searchRawWord(
        query: String
    ): DefaultResponse<RawSearchResponse> {
        return safeApiCall { api.searchRawWord(query) }
    }

    override suspend fun autoCompleteSearch(
        keyword: String
    ): DefaultResponse<List<AutoCompleteSearchKeyword>> {
        return safeApiCall { api.autoCompleteSearch(keyword) }
    }

    override suspend fun direct(
        from: Int,
        to: Int
    ): DefaultResponse<DirectionResponse> {
        return safeApiCall { api.direct(from, to) }
    }

    override suspend fun getBuildingDetail(buildingId: Int): DefaultResponse<FacilityInfo> {
        return safeApiCall { api.getBuildingDetail(buildingId) }
    }

    override suspend fun getFacilityDetail(facilityId: Int): DefaultResponse<FacilityInfo> {
        return safeApiCall { api.getFacilityDetail(facilityId) }
    }

    override suspend fun getFacilityReview(
        accessToken: String,
        facilityId: Int,
        page: Int,
        size: Int
    ): DefaultResponse<ReviewResponse> {
        return safeApiCall { api.getFacilityReview(accessToken, facilityId, page, size) }
    }

    override suspend fun getFacilityPhoto(
        accessToken: String,
        facilityId: Int,
        body: PhotoRequest
    ): DefaultResponse<PhotoResponse> {
        return safeApiCall { api.getFacilityPhoto(accessToken, facilityId, body) }
    }
}