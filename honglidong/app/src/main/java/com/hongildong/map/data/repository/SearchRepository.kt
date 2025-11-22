package com.hongildong.map.data.repository

import com.hongildong.map.data.entity.AutoCompleteSearchKeyword
import com.hongildong.map.data.entity.FacilityInfo
import com.hongildong.map.data.entity.NodeInfo
import com.hongildong.map.data.remote.request.PhotoRequest
import com.hongildong.map.data.remote.response.DirectionResponse
import com.hongildong.map.data.remote.response.PhotoResponse
import com.hongildong.map.data.remote.response.RawSearchResponse
import com.hongildong.map.data.remote.response.ReviewResponse
import com.hongildong.map.data.util.DefaultResponse

interface SearchRepository {
    suspend fun searchWithId(accessToken: String, nodeId: Long): DefaultResponse<NodeInfo>
    suspend fun searchRawWord(query: String): DefaultResponse<RawSearchResponse>
    suspend fun autoCompleteSearch(keyword: String): DefaultResponse<List<AutoCompleteSearchKeyword>>

    suspend fun direct(from: Int, to: Int): DefaultResponse<DirectionResponse>


    suspend fun getBuildingDetail(
        buildingId: Int
    ): DefaultResponse<FacilityInfo>

    // 시설 정보 검색: type이 facility인 경우
    suspend fun getFacilityDetail(
        facilityId: Int
    ): DefaultResponse<FacilityInfo>

    // 시설 리뷰 조회
    suspend fun getFacilityReview(
        accessToken: String, // 리뷰는 회원 기능
        facilityId: Int,
        page: Int, // 받아올 페이지 번호. 처음 받아올때는 0으로
        size: Int // 한번에 받아올 페이지의 크기 (리뷰 개수)
    ): DefaultResponse<ReviewResponse>

    // 시설 사진 조회
    suspend fun getFacilityPhoto(
        accessToken: String, // 사진도 회원 기능
        facilityId: Int,
        body: PhotoRequest
    ): DefaultResponse<PhotoResponse>
}