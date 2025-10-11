package com.hongildong.map.ui.util.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hongildong.map.data.entity.NodeInfo
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraAnimation
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.compose.CameraPositionState
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.LocationTrackingMode
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

    private val _pathNodes = MutableStateFlow<List<NodeInfo>>(emptyList())
    val pathNodes = _pathNodes.asStateFlow()

    // 카메라 따라가기 옵션을 뷰모델에서 관리 - 검색시 끄고 취소시 다시 켜는 용도
    private val _locationTrackingMode = MutableStateFlow(LocationTrackingMode.Follow)
    val locationTrackingMode = _locationTrackingMode.asStateFlow()

    @OptIn(ExperimentalNaverMapApi::class)
    fun showMarkerAndMoveCamera(position: LatLng, name: String) {
        viewModelScope.launch {
            // 카메라 이동 전 화면 위치 사용자 따라가는 옵션 삭제
            _locationTrackingMode.value = LocationTrackingMode.NoFollow
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

    fun showPath(nodes: List<NodeInfo>) {
        viewModelScope.launch {
            _locationTrackingMode.value = LocationTrackingMode.NoFollow
            _pathNodes.value = nodes
        }
    }

    fun clearMarker() {
        viewModelScope.launch {
            _locationTrackingMode.value = LocationTrackingMode.Follow
            _markers.value = listOf()
        }
    }
}