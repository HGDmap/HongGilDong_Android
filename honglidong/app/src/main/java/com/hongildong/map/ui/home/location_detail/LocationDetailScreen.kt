package com.hongildong.map.ui.home.location_detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.hongildong.map.ui.theme.AppTypography
import com.hongildong.map.ui.util.FlexibleBottomSheet
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.compose.CameraPositionState
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.Marker
import com.naver.maps.map.compose.MarkerState
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.compose.rememberCameraPositionState

@OptIn(ExperimentalMaterial3Api::class, ExperimentalNaverMapApi::class)
@Composable
fun LocationDetailScreen() {
    // todo: 건물 정보에서 건물명, 좌표 추출해서
    // 1. 네이버 맵 api 호출해 마커 띄우기
    // 2. 건물 정보 바텀 시트에 텍스트로 띄우기

    val sheetScaffoldState = rememberBottomSheetScaffoldState()

    val cameraPositionState: CameraPositionState = rememberCameraPositionState {
        // 카메라 초기 위치를 설정합니다.
        position = CameraPosition(LatLng(37.123, 127.123), 11.0)
    }

    NaverMap(
        cameraPositionState = cameraPositionState
    ) {
        Marker(
            state = MarkerState(position = LatLng(37.123, 127.123)),
            captionText = "여기에 캡션 텍스트"
        )
    }

    FlexibleBottomSheet(
        sheetScaffoldState = sheetScaffoldState
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            Text("건물명과 좌표 입력", style = AppTypography.Medium_15)
        }
    }
}