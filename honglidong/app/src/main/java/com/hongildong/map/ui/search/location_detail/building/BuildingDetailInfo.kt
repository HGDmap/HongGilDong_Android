package com.hongildong.map.ui.search.location_detail.building

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.hongildong.map.R
import com.hongildong.map.data.entity.FacilityInfo
import com.hongildong.map.data.entity.FloorFacility
import com.hongildong.map.ui.theme.AppTypography
import com.hongildong.map.ui.theme.Black
import com.hongildong.map.ui.theme.Gray500
import com.hongildong.map.ui.theme.Gray600
import com.hongildong.map.ui.theme.White
import com.hongildong.map.ui.util.ButtonWithIcon

@Composable
fun BuildingDetailInfo(
    buildingInfo: FacilityInfo,
    onDepart: () -> Unit,
    onArrival: () -> Unit,
    onClickFacility: (FloorFacility) -> Unit
) {
    val pages = listOf("층별 안내")
    var tabState by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        BuildingDetailHeader(
            searchResult = buildingInfo,
            onDepart = onDepart,
            onArrival = onArrival
        )
        Spacer(Modifier.height(16.dp))
        TabRow (
            // 1. 현재 선택된 탭의 인덱스
            selectedTabIndex = tabState,
            containerColor = White,
            contentColor = Black,
            // 2. 인디케이터 커스터마이징
            indicator = { tabPositions ->
                // 선택된 탭의 위치에만 검은색 선(Divider)을 그립니다.
                HorizontalDivider(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[tabState]),
                    thickness = 2.dp,
                    color = Black
                )
            },
            divider = {}
        ) {
            pages.forEachIndexed { index, title ->
                Tab(
                    selected = tabState == index,
                    onClick = { tabState = index },
                    text = {
                        Text(
                            text = title,
                            style = AppTypography.Medium_18,
                            maxLines = 1,
                            //modifier = Modifier.wrapContentSize(),
                            color = if (tabState == index) Black else Gray500
                        )
                    },
                    selectedContentColor = Black,
                    unselectedContentColor = Gray500,
                )
            }
        }
        when (tabState) {
            0 -> {
                // 시설 정보 탭
                BuildingFloorInfoTab(
                    buildingInfo,
                    onClickFacility = {
                        onClickFacility(it)
                    }
                )
            }
            else -> {
                BuildingFloorInfoTab(
                    buildingInfo,
                    onClickFacility = {
                        onClickFacility(it)
                    }
                )
            }
        }
    }
}

@Composable
fun BuildingDetailHeader(
    searchResult: FacilityInfo,
    onDepart: () -> Unit,
    onArrival: () -> Unit
) {

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = searchResult.name,
                style = AppTypography.Bold_22.copy(color = Black)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painterResource(
                        id = R.drawable.ic_share
                    ),
                    contentDescription = "",
                    modifier = Modifier
                        .clickable {}
                )
            }
        }
        Spacer(Modifier.height(8.dp))
        Text(
            text = searchResult.nodeName,
            style = AppTypography.Medium_13.copy(color = Gray600)
        )
        Spacer(Modifier.height(5.dp))
        Row(
            modifier = Modifier.align(Alignment.End)
        ) {
            ButtonWithIcon(
                icon = R.drawable.ic_departure,
                title = "출발",
                onClick = { onDepart() }
            )
            Spacer(Modifier.width(10.dp))
            ButtonWithIcon(
                icon = R.drawable.ic_arrival,
                title = "도착",
                onClick = { onArrival() }
            )
        }
    }
}