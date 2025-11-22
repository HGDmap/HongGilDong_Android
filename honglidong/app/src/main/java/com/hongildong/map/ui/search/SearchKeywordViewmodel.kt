package com.hongildong.map.ui.search

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hongildong.map.data.dao.SearchKeywordDao
import com.hongildong.map.data.entity.AutoCompleteSearchKeyword
import com.hongildong.map.data.entity.FacilityInfo
import com.hongildong.map.data.entity.NodeInfo
import com.hongildong.map.data.entity.ReviewInfo
import com.hongildong.map.data.entity.SearchKeyword
import com.hongildong.map.data.entity.SearchableNodeType
import com.hongildong.map.data.remote.request.PhotoRequest
import com.hongildong.map.data.remote.response.DirectionResponse
import com.hongildong.map.data.remote.response.PhotoResponse
import com.hongildong.map.data.remote.response.ReviewResponse
import com.hongildong.map.data.repository.ReviewRepository
import com.hongildong.map.data.repository.SearchRepository
import com.hongildong.map.data.util.DefaultResponse
import com.hongildong.map.data.util.ImageRepository
import com.hongildong.map.ui.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchKeywordViewmodel @Inject constructor(
    private val searchKeywordDao: SearchKeywordDao,
    private val searchRepository: SearchRepository,
    private val reviewRepository: ReviewRepository,
    private val imageRepository: ImageRepository,
    @ApplicationContext private val context: Context
): ViewModel() {

    private val TAG = this.javaClass.simpleName

    // db에 저장된 최근 검색어 목록 flow -> StateFlow 변환
    val recentKeywords = searchKeywordDao.getRecentKeywords()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _isSearchSuccess = MutableStateFlow<UiState>(UiState.Initial)
    val isSearchSuccess: StateFlow<UiState> = _isSearchSuccess.asStateFlow()

    private val _searchResult = MutableStateFlow<NodeInfo?>(null)
    val searchResult: StateFlow<NodeInfo?> = _searchResult.asStateFlow()

    private val _searchedList = MutableStateFlow<List<NodeInfo>>(listOf())
    val searchedList: StateFlow<List<NodeInfo>> = _searchedList.asStateFlow()

    private val _autoCompleteResult = MutableStateFlow<List<AutoCompleteSearchKeyword>>(listOf())
    val autoCompleteResult: StateFlow<List<AutoCompleteSearchKeyword>> = _autoCompleteResult.asStateFlow()

    private val _directionResult = MutableStateFlow<DirectionResponse?>(null)
    val directionResult: StateFlow<DirectionResponse?> = _directionResult.asStateFlow()

    private val _departPlaceInfo = MutableStateFlow<SearchKeyword?>(null)
    val departPlaceInfo: StateFlow<SearchKeyword?> = _departPlaceInfo.asStateFlow()

    private val _arrivalPlaceInfo = MutableStateFlow<SearchKeyword?>(null)
    val arrivalPlaceInfo: StateFlow<SearchKeyword?> = _arrivalPlaceInfo.asStateFlow()
    private val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    fun getToken(): String? {
        return sharedPreferences.getString("access_token", null)
    }

    // 출발지 지정
    fun setDepart(place: SearchKeyword) {
        viewModelScope.launch {
            _departPlaceInfo.value = place
            Log.d(TAG, "depart set: ${_departPlaceInfo.value}")
        }
    }

    // 도착지 지정
    fun setArrival(place: SearchKeyword) {
        viewModelScope.launch {
            _arrivalPlaceInfo.value = place
            Log.d(TAG, "arrival set: ${_arrivalPlaceInfo.value}")
        }
    }

    // 출발지 - 도착지 바꾸기
    fun exchangeDepartAndArrival() {
        viewModelScope.launch {
            val tempData = _departPlaceInfo.value
            _departPlaceInfo.value = _arrivalPlaceInfo.value
            _arrivalPlaceInfo.value = tempData
        }
    }

    // 출발지, 도착지 데이터 지우기 (direction 화면들에서 벗어날 때 초기화 필요)
    fun deleteDirectionData() {
        viewModelScope.launch {
            _departPlaceInfo.value = null
            _arrivalPlaceInfo.value = null
            _directionResult.value = null
        }
    }

    // 일반 검색 (검색 버튼으로 바로 검색, 자동완성 선택 x) 일때 호출
    fun onSearchRawWord(query: String) {
        if (query == "") {

            return
        }

        viewModelScope.launch {
            val response = searchRepository.searchRawWord(query)
            when (response) {
                is DefaultResponse.Success -> {
                    Log.d(TAG, "응답 성공: $response")
                    _searchedList.value = response.data.resultList
                    Log.d(TAG, "searched List: ${_searchedList.value}")
                    _isSearchSuccess.value = UiState.Success
                }
                is DefaultResponse.Error -> {
                    Log.d(TAG, "응답 실패: $response")
                    _isSearchSuccess.value = UiState.Error("유효하지 않은 검색어입니다.")
                }
            }
        }
    }

    fun onSearch(keyword: SearchKeyword) {
        viewModelScope.launch {
            when {
                keyword.nodeCode == SearchableNodeType.BUILDING.apiName -> {
                    onSearchBuildingInfo(keyword.id)
                }
                keyword.nodeCode == SearchableNodeType.FACILITY.apiName -> {
                    onSearchFacilityInfo(keyword.id)
                }
            }
        }
    }

    // 건물의 detail info
    private val _facilityDetail = MutableStateFlow<FacilityInfo?>(null)
    val facilityDetail = _facilityDetail.asStateFlow()

    // 건물의 detial info 받아오기
    fun onSearchFacilityInfo(facilityId: Int) {
        viewModelScope.launch {
            /*val token = getToken()
            if (token == null) {
                Log.e(TAG, "토큰이 없습니다")
                return@launch
            }*/

            val response = searchRepository.getFacilityDetail(facilityId = facilityId)
            when (response) {
                is DefaultResponse.Success -> {
                    Log.d(TAG, "응답 성공: $response")
                    _facilityDetail.value = response.data
                    Log.d(TAG, "searched facility detail: ${_facilityDetail.value}")
                    _isSearchSuccess.value = UiState.Success

                    searchKeywordDao.insertKeyword(
                        SearchKeyword(
                            nodeName = response.data.name,
                            nodeId = response.data.nodeId,
                            nodeCode = response.data.type,
                            id = response.data.id
                        )
                    )
                }
                is DefaultResponse.Error -> {
                    Log.d(TAG, "응답 실패: $response")
                    _isSearchSuccess.value = UiState.Error("유효하지 않은 검색어입니다.")
                }
            }
        }
    }

    private val _searchedBuildingInfo = MutableStateFlow<FacilityInfo?>(null)
    val searchedBuildingInfo: StateFlow<FacilityInfo?> = _searchedBuildingInfo.asStateFlow()

    // 검색시 호출
    fun onSearchBuildingInfo(id: Int) {
        viewModelScope.launch {
            val response = searchRepository.getBuildingDetail(id)

            when (response) {
                is DefaultResponse.Success -> {
                    Log.d(TAG, "응답 성공: $response")
                    _searchedBuildingInfo.value = response.data
                    Log.d(TAG, "searchResult: ${searchedBuildingInfo.value}")
                    _isSearchSuccess.value = UiState.Success

                    searchKeywordDao.insertKeyword(
                        SearchKeyword(
                            nodeName = response.data.name,
                            nodeId = response.data.nodeId,
                            nodeCode = response.data.type,
                            id = response.data.id
                        )
                    )
                }
                is DefaultResponse.Error -> {
                    Log.d(TAG, "응답 실패: $response")
                    _isSearchSuccess.value = UiState.Error("유효하지 않은 검색어입니다.")
                }
            }
        }
    }

    // 검색 자동완성 로직
    fun autoCompleteSearch(keyword: String) {
        if (keyword.isEmpty()) return

        viewModelScope.launch {
            val response = searchRepository.autoCompleteSearch(keyword)
            when (response) {
                is DefaultResponse.Success -> {
                    Log.d(TAG, "응답 성공: $response")
                    _autoCompleteResult.value = response.data
                    Log.d(TAG, "autoCompleteResult: ${_autoCompleteResult.value}")
                    _isSearchSuccess.value = UiState.Success
                }
                is DefaultResponse.Error -> {
                    Log.d(TAG, "응답 실패: $response")
                    _isSearchSuccess.value = UiState.Error("유효하지 않은 검색어입니다.")
                }
            }
        }
    }

    // 검색어 1개 삭제
    fun deleteKeyword(keyword: SearchKeyword) {
        viewModelScope.launch {
            searchKeywordDao.deleteKeyword(
                id = keyword.id,
                nodeCode = keyword.nodeCode
            )
        }
    }

    // 전체 검색어 삭제
    fun clearAllKeyword() {
        viewModelScope.launch {
            searchKeywordDao.clearAllKeywords()
        }
    }

    // 길찾기 api 임시 연결
    fun direct() {
        if ((_departPlaceInfo.value == null) and (_arrivalPlaceInfo.value == null)) {
            Toast.makeText(context, "잘못된 접근입니다", Toast.LENGTH_SHORT).show()
            return
        }
        viewModelScope.launch {
            Log.d(TAG, "길찾기 from=${_departPlaceInfo.value} to=${_arrivalPlaceInfo.value}")
            val response = searchRepository.direct(
                from = _departPlaceInfo.value!!.nodeId,
                to = _arrivalPlaceInfo.value!!.nodeId
            )
            when (response) {
                is DefaultResponse.Success -> {
                    Log.d(TAG, "응답 성공: $response")
                    _directionResult.value = response.data
                    Log.d(TAG, "directionResult: ${directionResult.value}")
                    _isSearchSuccess.value = UiState.Success
                }
                is DefaultResponse.Error -> {
                    Log.d(TAG, "응답 실패: $response")
                    _isSearchSuccess.value = UiState.Error(response.message)
                }
            }
        }
    }

    private val _facilityPhotoInfo = MutableStateFlow<PhotoResponse?>(null)
    val facilityPhotoInfo = _facilityPhotoInfo.asStateFlow()

    // 시설 사진 받기 api
    fun getFacilityPhotos(
        facilityId: Int,
    ) {
        viewModelScope.launch {
            val token = getToken()
            if (token == null) {
                Log.e(TAG, "토큰이 없습니다")
                return@launch
            }

            var request: PhotoRequest
            if (_facilityPhotoInfo.value == null) {
                // 처음 요청시
                request = PhotoRequest(
                    size = 20
                )
            } else {
                if (_facilityPhotoInfo.value!!.isLast) {
                    // 다음 요청시
                    request = PhotoRequest(
                        continuationToken = _facilityPhotoInfo.value!!.continuationToken,
                        size = 20
                    )
                } else {
                    // 다음 요청 - 마지막 페이지의 경우
                    Toast.makeText(context, "마지막 페이지입니다.", Toast.LENGTH_SHORT).show()
                    return@launch
                }
            }

            val response = searchRepository.getFacilityPhoto(token, facilityId, request)
            when (response) {
                is DefaultResponse.Success -> {
                    Log.d(TAG, "응답 성공: $response")
                    _facilityPhotoInfo.value = response.data
                    Log.d(TAG, "_facilityPhotoInfo: ${_facilityPhotoInfo.value}")
                    _isSearchSuccess.value = UiState.Success
                }
                is DefaultResponse.Error -> {
                    Log.d(TAG, "응답 실패: $response")
                    _isSearchSuccess.value = UiState.Error(response.message)
                }
            }
        }
    }

    // 현재 리뷰 페이지
    private val _reviewPage = MutableStateFlow<Int>(0)

    // 시설 리뷰 리스트
    private val _facilityReviewInfo = MutableStateFlow<ReviewResponse?>(null)
    val facilityReviewInfo = _facilityReviewInfo.asStateFlow()

    // 시설 리뷰
    private val _facilityReviews = MutableStateFlow<List<ReviewInfo>>(listOf())
    val facilityReviews = _facilityReviews.asStateFlow()

    // 시설 리뷰 조회
    fun getFacilityReview(facilityId: Int) {
        viewModelScope.launch {
            val token = getToken()
            if (token == null) {
                Log.e(TAG, "토큰이 없습니다")
                return@launch
            }

            /*if (_facilityReviewInfo.value != null) {
                if (!_facilityReviewInfo.value!!.isLast) {
                    // 다음 요청 - 마지막 페이지의 경우
                    Toast.makeText(context, "마지막 페이지입니다.", Toast.LENGTH_SHORT).show()
                    return@launch
                }
                // 다음 요청 - 마지막 페이지가 아니라면
                _reviewPage.value += 1
            } else {
                // 처음 요청
                _reviewPage.value = 0
            }*/

            val response = searchRepository.getFacilityReview(token, facilityId, 0, 20)
            when (response) {
                is DefaultResponse.Success -> {
                    Log.d(TAG, "응답 성공: $response")
                    _facilityReviewInfo.value = response.data
                    _facilityReviews.value = response.data.reviewList
                    Log.d(TAG, "_facilityReviewInfo: ${_facilityReviewInfo.value}")
                    _isSearchSuccess.value = UiState.Success
                }
                is DefaultResponse.Error -> {
                    Log.d(TAG, "응답 실패: $response")
                    _isSearchSuccess.value = UiState.Error(response.message)
                }
            }
        }
    }

    fun updateLikedReview(
        reviewId: Int
    ) {
        viewModelScope.launch {
            val token = getToken() ?: return@launch

            val response = reviewRepository.saveReview(token, reviewId)
            when (response) {
                is DefaultResponse.Success -> {
                    Log.d(TAG, "리뷰 좋아요 성공 $response")
                }
                is DefaultResponse.Error -> {
                    Log.d(TAG, "리뷰 좋아요 실패 $response")
                }
            }
        }
    }

    fun eraseFacilityData() {
        viewModelScope.launch {
            _facilityPhotoInfo.value = null
            _facilityDetail.value = null
            _reviewPage.value = 0
            _facilityReviewInfo.value = null
        }
    }
}