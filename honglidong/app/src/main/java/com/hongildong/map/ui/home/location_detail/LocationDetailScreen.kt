package com.hongildong.map.ui.home.location_detail

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.hongildong.map.ui.home.search.SearchKeywordViewmodel
import com.hongildong.map.ui.theme.AppTypography
import com.hongildong.map.ui.util.FlexibleBottomSheet
import com.hongildong.map.ui.util.map.MapViewmodel
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.compose.ExperimentalNaverMapApi

@OptIn(ExperimentalMaterial3Api::class, ExperimentalNaverMapApi::class)
@Composable
fun LocationDetailScreen(
    mainNavController: NavController,
    searchViewmodel: SearchKeywordViewmodel,
    mapViewmodel: MapViewmodel
) {

    val searchResult by searchViewmodel.searchResult.collectAsState()
    LaunchedEffect(key1 = searchResult) {
        // searchResult가 null이 아닐 때만 실행
        searchResult?.let { result ->
            val newPosition = LatLng(result.latitude, result.longitude)
            mapViewmodel.showMarkerAndMoveCamera(newPosition, result.name)
        }
    }

    // todo: 건물 정보에서 건물명, 좌표 추출해서
    // 1. 네이버 맵 api 호출해 마커 띄우기
    // 2. 건물 정보 바텀 시트에 텍스트로 띄우기

    val sheetScaffoldState = rememberBottomSheetScaffoldState()

    Box (
        modifier = Modifier.fillMaxSize()
    ) {
        FlexibleBottomSheet(
            sheetScaffoldState = sheetScaffoldState
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top
            ) {
                Text(searchResult?.name ?: "건물명과 좌표 입력", style = AppTypography.Medium_15)
                Text("위도: ${searchResult?.latitude} / 경도: ${searchResult?.longitude}", style = AppTypography.Medium_15)
            }
        }
    }
}