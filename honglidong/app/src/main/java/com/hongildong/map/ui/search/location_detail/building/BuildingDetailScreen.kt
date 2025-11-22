package com.hongildong.map.ui.search.location_detail.building

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import com.hongildong.map.data.entity.FacilityInfo
import com.hongildong.map.data.entity.FloorFacility
import com.hongildong.map.data.entity.SearchableNodeType
import com.hongildong.map.data.entity.toSearchKeyword
import com.hongildong.map.ui.search.SearchKeywordViewmodel
import com.hongildong.map.ui.search.location_detail.SearchBarWithGoBack
import com.hongildong.map.ui.util.bottomsheet.AnchoredDraggableBottomSheet
import com.hongildong.map.ui.util.map.MapViewmodel
import com.naver.maps.geometry.LatLng

@Composable
fun BuildingDetailScreen(
    searchedBuildingId: Int,
    searchedWord: String,
    searchViewmodel: SearchKeywordViewmodel,
    mapViewmodel: MapViewmodel,
    onGoBack: () -> Unit,
    onSearchDirection: () -> Unit,
    onClickFacility: (FloorFacility) -> Unit
) {
    val context = LocalContext.current
    val buildingInfo by searchViewmodel.searchedBuildingInfo.collectAsState()

    LaunchedEffect(Unit) {
        // 검색 결과 바탕으로 시설 상세 정보 api 호출
        searchViewmodel.onSearchBuildingInfo(searchedBuildingId)
    }

    LaunchedEffect(key1 = buildingInfo) {

        // facilityInfo가 null이 아닐 때만 실행
        buildingInfo?.let { result ->
            val newPosition = LatLng(result.latitude, result.longitude)
            mapViewmodel.showMarkerAndMoveCamera(newPosition, result.name)
        }
    }

    val nestedScrollConnection = rememberNestedScrollInteropConnection()

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
                BuildingDetailInfo(
                    buildingInfo ?: FacilityInfo(
                        description = "temp",
                        name = "temp building",
                        id = 0,
                        isBookmarked = false,
                        latitude = 0.0,
                        link = "",
                        longitude = 0.0,
                        nodeId = 0,
                        nodeName = "",
                        open = "",
                        phone = "",
                        photoList = emptyList(),
                        type = SearchableNodeType.BUILDING.apiName,
                        floorFacilities = emptyList()
                    ),
                    onDepart = {
                        if (buildingInfo != null) {
                            // 검색 결과를 바탕으로 출발지 설정
                            val keyword = buildingInfo!!.toSearchKeyword()

                            // 출발지 설정
                            searchViewmodel.setDepart(keyword)
                            // 경로 검색 화면으로 화면 전환
                            onSearchDirection()
                        } else Toast.makeText(context, "유효하지 않은 장소입니다.", Toast.LENGTH_SHORT).show()
                    },
                    onArrival = {
                        if (buildingInfo != null) {
                            // 검색 결과를 바탕으로 도착지 설정
                            val keyword = buildingInfo!!.toSearchKeyword()

                            // 도착지 설정
                            searchViewmodel.setArrival(keyword)
                            // 경로 검색 화면으로 화면 전환
                            onSearchDirection()
                        } else Toast.makeText(context, "유효하지 않은 장소입니다.", Toast.LENGTH_SHORT).show()
                    },
                    onClickFacility = {
                        onClickFacility(it)
                    }
                )
            }
        }
    }
}