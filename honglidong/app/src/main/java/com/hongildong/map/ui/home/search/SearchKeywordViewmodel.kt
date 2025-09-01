package com.hongildong.map.ui.home.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hongildong.map.data.dao.SearchKeywordDao
import com.hongildong.map.data.entity.SearchKeyword
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchKeywordViewmodel @Inject constructor(
    private val searchKeywordDao: SearchKeywordDao
): ViewModel() {

    // db에 저장된 최근 검색어 목록 flow -> StateFlow 변환
    val recentKeywords = searchKeywordDao.getRecentKeywords()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // 검색시 호출
    fun onSearch(keyword: String) {
        if (keyword.isBlank()) return

        viewModelScope.launch {
            searchKeywordDao.insertKeyword(
                SearchKeyword(
                    keyword = keyword.trim()
                )
            )
        }

        // todo: 실제 검색 로직 -> 백엔드 연결시 추가
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