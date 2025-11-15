package com.hongildong.map.ui.search

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.key
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hongildong.map.data.dao.SearchKeywordDao
import com.hongildong.map.data.entity.AutoCompleteSearchKeyword
import com.hongildong.map.data.entity.FacilityInfo
import com.hongildong.map.data.entity.NodeInfo
import com.hongildong.map.data.entity.SearchKeyword
import com.hongildong.map.data.entity.SearchableNodeType
import com.hongildong.map.data.remote.request.PhotoRequest
import com.hongildong.map.data.remote.response.DirectionResponse
import com.hongildong.map.data.remote.response.PhotoResponse
import com.hongildong.map.data.repository.SearchRepository
import com.hongildong.map.data.util.DefaultResponse
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
                    onSearchBuildingInfo(keyword)
                }
                keyword.nodeCode == SearchableNodeType.FACILITY.apiName -> {
                    onSearchFacilityInfo(keyword)
                }
            }
        }
    }

    // 건물의 detail info
    private val _facilityDetail = MutableStateFlow<FacilityInfo?>(null)
    val facilityDetail = _facilityDetail.asStateFlow()

    // 건물의 detial info 받아오기
    fun onSearchFacilityInfo(keyword: SearchKeyword) {
        viewModelScope.launch {
            /*val token = getToken()
            if (token == null) {
                Log.e(TAG, "토큰이 없습니다")
                return@launch
            }*/

            val response = searchRepository.getFacilityDetail(keyword.id)
            when (response) {
                is DefaultResponse.Success -> {
                    Log.d(TAG, "응답 성공: $response")
                    _facilityDetail.value = response.data
                    Log.d(TAG, "searched facility detail: ${_facilityDetail.value}")
                    _isSearchSuccess.value = UiState.Success

                    searchKeywordDao.insertKeyword(
                        SearchKeyword(
                            nodeName = response.data.nodeName,
                            nodeId = response.data.id,
                            nodeCode = response.data.type,
                            id = response.data.nodeId
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

    // 검색시 호출
    fun onSearchBuildingInfo(keyword: SearchKeyword) {
        viewModelScope.launch {

            // 임시 검색 로직
            // todo: 실제 검색 로직이 생기면 바꿀 것
            val token = getToken()
            if (token == null) {
                Log.e(TAG, "토큰이 없습니다")
                return@launch
            }

            val keywordValue = keyword.nodeId.toLong()
            if (keywordValue == null) {
                _isSearchSuccess.value = UiState.Error("유효하지 않은 검색어입니다.")
                return@launch
            }
            // todo: 타입별로 검색 api 달라지면 when절로 타입별 검색하기
            val response = searchRepository.searchWithId(token, keywordValue)
            when (response) {
                is DefaultResponse.Success -> {
                    Log.d(TAG, "응답 성공: $response")
                    _searchResult.value = response.data
                    Log.d(TAG, "searchResult: ${searchResult.value}")
                    _isSearchSuccess.value = UiState.Success

                    searchKeywordDao.insertKeyword(
                        SearchKeyword(
                            nodeName = response.data.name ?: "temp",
                            nodeId = response.data.nodeId,
                            nodeCode = response.data.nodeCode ?: "",
                            id = response.data.nodeId
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

    val _facilityPhotoInfo = MutableStateFlow<PhotoResponse?>(null)
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

            var request = PhotoRequest("",0)
            if (_facilityPhotoInfo.value == null) {
                request = PhotoRequest(
                    continuationToken = "",
                    size = 20
                )
            } else {
                request = PhotoRequest(
                    continuationToken = _facilityPhotoInfo.value!!.continuationToken,
                    size = 20
                )
            }

            val response = searchRepository.getFacilityPhoto(token, facilityId, request)
            when (response) {
                is DefaultResponse.Success -> {
                    Log.d(TAG, "응답 성공: $response")
                    _facilityPhotoInfo.value = response.data
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
}