package com.hongildong.map.ui.util.map

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.hongildong.map.R
import com.hongildong.map.data.entity.FolderColor
import com.hongildong.map.data.entity.NodeInfo
import com.hongildong.map.ui.theme.BookmarkRed
import com.hongildong.map.ui.theme.Gray600
import com.hongildong.map.ui.theme.PrimaryMid
import com.hongildong.map.ui.theme.White
import com.hongildong.map.ui.util.BookmarkIcon
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.compose.CircleOverlay
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.MapProperties
import com.naver.maps.map.compose.MapUiSettings
import com.naver.maps.map.compose.Marker
import com.naver.maps.map.compose.MarkerComposable
import com.naver.maps.map.compose.MarkerState
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.compose.PathOverlay
import com.naver.maps.map.compose.rememberFusedLocationSource
import com.naver.maps.map.overlay.OverlayImage

@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun MapBackground(
    viewModel: MapViewmodel,
    onClickBookmark: () -> Unit
) {
    // 마커
    val markers by viewModel.markers.collectAsState()
    // 길찾기
    val pathNodes by viewModel.pathNodes.collectAsState()
    // 북마크
    val bookmarks by viewModel.bookmarks.collectAsState()
    // 사용자 위치 따라가기 모드 제어용
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

        bookmarks.forEach { folderInfo ->
            val folderColor = FolderColor.fromColorName(folderInfo.color)?.color ?: BookmarkRed
            folderInfo.bookmarkList.forEach { bookmark ->
                MarkerComposable(
                    state = MarkerState(position = LatLng(bookmark.latitude, bookmark.longitude)),
                    captionText = bookmark.name ?: bookmark.nodeName ?: "",
                    onClick = {
                        // todo: 북마크 버블 클릭시 해당 건물/시설 검색해야함
                        false
                    }
                ) {
                    BookmarkIcon(
                        size = 20.dp,
                        color = folderColor
                    )
                }
            }
        }

        // 길찾기
        if (pathNodes.size > 2) {
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