package com.hongildong.map.ui.home.search

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hongildong.map.data.dao.SearchKeywordDao
import com.hongildong.map.data.entity.NodeInfo
import com.hongildong.map.data.entity.SearchKeyword
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

    private val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    fun getToken(): String? {
        return sharedPreferences.getString("access_token", null)
    }

    // 검색시 호출
    fun onSearch(keyword: String) {
        if (keyword.isBlank()) return

        viewModelScope.launch {

            searchKeywordDao.insertKeyword(
                SearchKeyword(
                    keyword = keyword.trim()
                )
            )

            // 임시 검색 로직
            // todo: 실제 검색 로직이 생기면 바꿀 것
            val token = getToken()
            if (token == null) {
                Log.e(TAG, "토큰이 없습니다")
                return@launch
            }

            val keywordValue = keyword.trim().toLongOrNull()
            if (keywordValue == null) {
                _isSearchSuccess.value = UiState.Error("유효하지 않은 검색어입니다.")
                return@launch
            }
            val response = searchRepository.searchWithId(token, keywordValue)
            when (response) {
                is DefaultResponse.Success -> {
                    Log.d(TAG, "응답 성공: $response")
                    _searchResult.value = response.data
                    Log.d(TAG, "searchResult: ${searchResult.value}l")
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
    fun deleteKeyword(keyword: String) {
        viewModelScope.launch {
            searchKeywordDao.deleteKeyword(keyword)
        }
    }

    // 전체 검색어 삭제
    fun clearAllKeyword() {
        viewModelScope.launch {
            searchKeywordDao.clearAllKeywords()
        }
    }
}