package com.hongildong.map.ui.util.map

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.LocationTrackingMode
import com.naver.maps.map.compose.MapProperties
import com.naver.maps.map.compose.MapUiSettings
import com.naver.maps.map.compose.Marker
import com.naver.maps.map.compose.MarkerState
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.compose.rememberFusedLocationSource

@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun MapBackground(
    viewModel: MapViewmodel
) {
    val markers by viewModel.markers.collectAsState()
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
        markers.forEach { markerInfo ->
            Log.d("mapviewmodel", "$markerInfo")
            Marker(
                state = MarkerState(position = markerInfo.position),
                captionText = markerInfo.name
            )
        }
    }
}