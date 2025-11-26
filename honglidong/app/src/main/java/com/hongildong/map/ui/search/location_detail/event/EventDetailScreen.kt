package com.hongildong.map.ui.search.location_detail.event

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import com.hongildong.map.data.entity.EventDetailInfo
import com.hongildong.map.data.entity.SearchKeyword
import com.hongildong.map.data.entity.SearchableNodeType
import com.hongildong.map.data.entity.toSearchKeyword
import com.hongildong.map.ui.search.SearchKeywordViewmodel
import com.hongildong.map.ui.search.location_detail.SearchBarWithGoBack
import com.hongildong.map.ui.util.CustomLoading
import com.hongildong.map.ui.util.bottomsheet.AnchoredDraggableBottomSheet
import com.hongildong.map.ui.util.map.MapViewmodel
import com.naver.maps.map.compose.ExperimentalNaverMapApi

@OptIn(ExperimentalMaterial3Api::class, ExperimentalNaverMapApi::class)
@Composable
fun EventDetailScreen(
    searchedWord: String,
    searchedEventId: Int,
    onGoBack: () -> Unit,
    mapViewmodel: MapViewmodel,
    searchViewmodel: SearchKeywordViewmodel,
    onClickEventLocation: (EventDetailInfo) -> Unit = {},
    onDirectEventLocation: (EventDetailInfo) -> Unit = {}
) {
    // 시스템 뒤로가기 버튼 - 커스텀 동작과 연결
    BackHandler {
        onGoBack()
    }

    val context = LocalContext.current

    val nestedScrollConnection = rememberNestedScrollInteropConnection()

    val eventInfo by searchViewmodel.searchedEventInfo.collectAsState()

    LaunchedEffect(Unit) {
        searchViewmodel.onSearchEventInfo(searchedEventId)
    }

    if (eventInfo == null) {
        CustomLoading()
    } else {
        Box (
            Modifier.background(Color.Transparent)
        ) {
            SearchBarWithGoBack(
                searchedWord = searchedWord,
                onGoBack = {
                    onGoBack()
                    mapViewmodel.clearMarker()
                }
            )

            BoxWithConstraints (
                modifier = Modifier.fillMaxSize()
            ) {
                val fullHeight = constraints.maxHeight.toFloat()

                AnchoredDraggableBottomSheet (
                    modifier = Modifier
                        .nestedScroll(nestedScrollConnection),
                    maxHeight = fullHeight,
                    isFullScreen = true
                ) {
                    EventDetailInfo(
                        eventInfo = eventInfo!!,
                        onClickEventLocation = {
                            onClickEventLocation(it)
                        },
                        onDirectEventLocation = {
                            // 검색 결과를 바탕으로 도착지 설정
                            val keyword = SearchKeyword(
                                nodeName = eventInfo!!.locationInfo.buildingName,
                                id = eventInfo!!.locationInfo.nodeId,
                                nodeId = eventInfo!!.locationInfo.nodeId,
                                nodeCode = SearchableNodeType.BUILDING.apiName,
                            )

                            // 도착지 설정
                            searchViewmodel.setArrival(keyword)
                            // 경로 검색 화면으로 화면 전환
                            onDirectEventLocation(it)
                        }
                    )
                }
            }
        }
    }
}
