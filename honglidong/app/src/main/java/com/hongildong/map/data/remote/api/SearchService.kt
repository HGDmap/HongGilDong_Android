package com.hongildong.map.data.remote.api

import com.hongildong.map.data.entity.AutoCompleteSearchKeyword
import com.hongildong.map.data.entity.FacilityInfo
import com.hongildong.map.data.entity.NodeInfo
import com.hongildong.map.data.remote.request.ImageUploadRequest
import com.hongildong.map.data.remote.request.PhotoRequest
import com.hongildong.map.data.remote.response.DirectionResponse
import com.hongildong.map.data.remote.response.ImageUploadResponse
import com.hongildong.map.data.remote.response.PhotoResponse
import com.hongildong.map.data.remote.response.RawSearchResponse
import com.hongildong.map.data.remote.response.ReviewResponse
import com.hongildong.map.data.util.ApiResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface SearchService {
    // 임시 검색 로직 - 나중에 facility id로 검색하는 api 연결 예정
    @GET("node/{nodeId}")
    suspend fun searchWithId(
        @Header("Authorization") accessToken: String,
        @Path("nodeId") nodeId: Long
    ): ApiResponse<NodeInfo>

    // 시설 정보 검색: type이 facility인 경우
    @GET("facility/{facilityId}/details")
    suspend fun getFacilityDetail(
        @Path("facilityId") facilityId: Int
    ): ApiResponse<FacilityInfo>

    // 시설 리뷰 조회
    @GET("facility/{facilityId}/reviews")
    suspend fun getFacilityReview(
        @Header("Authorization") accessToken: String, // 리뷰는 회원 기능
        @Path("facilityId") facilityId: Int,
        @Query("page") page: Int, // 받아올 페이지 번호. 처음 받아올때는 0으로
        @Query("size") size: Int // 한번에 받아올 페이지의 크기 (리뷰 개수)
    ): ApiResponse<ReviewResponse>

    // 시설 사진 조회
    @POST("facility/{facilityId}/photos")
    suspend fun getFacilityPhoto(
        @Header("Authorization") accessToken: String, // 사진도 회원 기능
        @Path("facilityId") facilityId: Int,
        @Body body: PhotoRequest
    ): ApiResponse<PhotoResponse>

    // 자동완성이 아닌 검색
    @GET("search/{query}")
    suspend fun searchRawWord(
        @Path("query") query: String
    ): ApiResponse<RawSearchResponse>

    // 검색 자동완성 api
    @GET("search/list/{keyword}")
    suspend fun autoCompleteSearch(
        @Path("keyword") keyword: String
    ): ApiResponse<List<AutoCompleteSearchKeyword>>


    // 길찾기 api
    @GET("directions")
    suspend fun direct(
        @Query("from") from: Int,
        @Query("to") to: Int
    ): ApiResponse<DirectionResponse>
}