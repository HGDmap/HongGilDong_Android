package com.hongildong.map.ui.search.location_detail

import android.annotation.SuppressLint
import android.view.View
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.hongildong.map.R
import com.hongildong.map.ui.search.SearchKeywordViewmodel
import com.hongildong.map.ui.theme.AppTypography
import com.hongildong.map.ui.theme.Gray100
import com.hongildong.map.ui.theme.Gray400
import com.hongildong.map.ui.theme.White
import com.hongildong.map.ui.util.AnchoredDraggableBottomSheet
import com.hongildong.map.ui.util.map.MapViewmodel
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.compose.ExperimentalNaverMapApi

@SuppressLint("UnusedBoxWithConstraintsScope")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalNaverMapApi::class)
@Composable
fun LocationDetailScreen(
    searchedWord: String = "",
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
    Box (
        Modifier.background(Color.Transparent)
    ) {
        SearchBarWithGoBack(
            searchedWord = searchedWord,
            onGoBack = {mainNavController.popBackStack()}
        )

        BoxWithConstraints (
            modifier = Modifier.fillMaxSize()
        ) {
            val fullHeight = constraints.maxHeight.toFloat()
            AnchoredDraggableBottomSheet (
                modifier = Modifier,
                maxHeight = fullHeight,
                isFullScreen = true
            ) {
                Column (
                    modifier = Modifier.fillMaxSize().weight(1f)
                ) {
                    Text(searchResult?.name ?: "건물명과 좌표 입력", style = AppTypography.Medium_15)
                    Text("위도: ${searchResult?.latitude} / 경도: ${searchResult?.longitude}", style = AppTypography.Medium_15)
                }
            }
        }
    }
}

@Composable
fun SearchBarWithGoBack(
    searchedWord: String,
    onGoBack: () -> Unit
) {
    Row(
        modifier = Modifier
            .background(White)
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.statusBars)
            .padding(top = 16.dp, start = 10.dp, end = 10.dp, bottom = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        Image(
            painter = painterResource(R.drawable.ic_back),
            contentDescription = stringResource(R.string.go_back),
            modifier = Modifier
                .clickable {
                    onGoBack()
                }
        )
        Spacer(
            modifier = Modifier.width(15.dp)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(53.dp)
                .background(color = White, shape = RoundedCornerShape(size = 10.dp))
                .border(1.dp, color = Gray400, shape = RoundedCornerShape(size = 10.dp)),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = searchedWord,
                color = Gray400,
                style = AppTypography.Regular_15,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
    }
}