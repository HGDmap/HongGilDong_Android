package com.hongildong.map.ui.util.map

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hongildong.map.R
import com.hongildong.map.ui.theme.Gray600
import com.hongildong.map.ui.theme.PrimaryMid
import com.hongildong.map.ui.theme.White
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.MapProperties
import com.naver.maps.map.compose.MapUiSettings
import com.naver.maps.map.compose.Marker
import com.naver.maps.map.compose.MarkerState
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.compose.PathOverlay
import com.naver.maps.map.compose.rememberFusedLocationSource
import com.naver.maps.map.overlay.OverlayImage

@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun MapBackground(
    viewModel: MapViewmodel
) {
    val markers by viewModel.markers.collectAsState()
    val pathNodes by viewModel.pathNodes.collectAsState()
    val locationTrackingMode by viewModel.locationTrackingMode.collectAsState()

    NaverMap(
        modifier = Modifier.fillMaxSize(),
        locationSource = rememberFusedLocationSource(),
        properties = MapProperties(
            locationTrackingMode = locationTrackingMode
        ),
        uiSettings = MapUiSettings(
            isLocationButtonEnabled = true,
        ),
        cameraPositionState = viewModel.cameraPositionState
    ) {
        // 마커
        markers.forEach { markerInfo ->
            Marker(
                state = MarkerState(position = markerInfo.position),
                captionText = markerInfo.name
            )
        }
        // 길찾기
        if (pathNodes.isNotEmpty()) {
            PathOverlay(
                coords = pathNodes,
                width = 6.dp,
                color = PrimaryMid,
                patternImage = OverlayImage.fromResource(R.drawable.ic_path_pattern),
                patternInterval = 10.dp,
                outlineColor = White,
                passedColor = Gray600,
            )
        }
    }
}