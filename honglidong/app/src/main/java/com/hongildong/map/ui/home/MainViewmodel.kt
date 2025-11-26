package com.hongildong.map.ui.home

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hongildong.map.data.entity.EventBriefInfo
import com.hongildong.map.data.remote.response.RecommendPlace
import com.hongildong.map.data.repository.MainRepository
import com.hongildong.map.data.util.DefaultResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewmodel @Inject constructor(
    private val mainRepository: MainRepository,
    @ApplicationContext private val context: Context
): ViewModel() {
    private val TAG = this.javaClass.simpleName

    // 토큰
    private val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    fun getToken(): String? {
        return sharedPreferences.getString("access_token", null)
    }

    private val _recommendLocations = MutableStateFlow<List<RecommendPlace>>(emptyList())
    val recommendLocations = _recommendLocations.asStateFlow()

    fun getRecommendLocations() {
        viewModelScope.launch {
            val token = getToken()
            val response = mainRepository.getRecommendPlaces(token)

            when (response) {
                is DefaultResponse.Success -> {
                    Log.d(TAG, "추천 시설 조회 성공: $response")
                    _recommendLocations.value = response.data.recommendedFacilityList
                }
                is DefaultResponse.Error -> {
                    // 에러 처리
                    Log.d(TAG, "추천 시설 조회 실패: $response")
                }
            }
        }
    }

    private val _allEventInfo = MutableStateFlow<List<EventBriefInfo>>(emptyList())
    val allEventInfo = _allEventInfo.asStateFlow()

    fun getAllEvents() {
        viewModelScope.launch {
            val response = mainRepository.getAllEvents()

            when (response) {
                is DefaultResponse.Success -> {
                    Log.d(TAG, "모든 이벤트 불러오기 성공: $response")
                    _allEventInfo.value = response.data.events
                }
                is DefaultResponse.Error -> {
                    Log.d(TAG, "모든 이벤트 불러오기 실패: $response")
                }
            }
        }
    }
}