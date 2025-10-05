package com.hongildong.map.ui.util.map

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraAnimation
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.compose.CameraPositionState
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MarkerInfo(
    val key: Long,
    val position: LatLng,
    val name: String
)

@HiltViewModel
class MapViewmodel @Inject constructor(

): ViewModel() {
    // 지도 카메라의 상태를 ViewModel에서 직접 관리합니다.
    val cameraPositionState = CameraPositionState()

    // 지도에 표시될 마커 목록을 StateFlow로 관리합니다.
    private val _markers = MutableStateFlow<List<MarkerInfo>>(emptyList())
    val markers = _markers.asStateFlow()

    @OptIn(ExperimentalNaverMapApi::class)
    fun showMarkerAndMoveCamera(position: LatLng, name: String) {
        viewModelScope.launch {
            // 마커 목록 업데이트
            _markers.value = listOf(MarkerInfo(0, position, name))

            // 카메라 이동
            val currentZoom = cameraPositionState.position.zoom
            val targetZoom = 17.0
            val cameraUpdate = CameraUpdate
                .toCameraPosition(
                CameraPosition(position, targetZoom)
                ).animate(CameraAnimation.Easing)
            cameraPositionState.move(cameraUpdate)
        }
    }

    fun clearMarker() {
        viewModelScope.launch {
            _markers.value = listOf()
        }
    }
}