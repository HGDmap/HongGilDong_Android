package com.hongildong.map.ui.search.location_detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
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
import com.hongildong.map.data.entity.NodeInfo
import com.hongildong.map.ui.home.RecommendPlaceItem
import com.hongildong.map.ui.home.places
import com.hongildong.map.ui.theme.AppTypography
import com.hongildong.map.ui.theme.Black
import com.hongildong.map.ui.theme.Gray500
import com.hongildong.map.ui.theme.Gray600
import com.hongildong.map.ui.theme.PrimaryMid
import com.hongildong.map.ui.theme.White
import com.hongildong.map.ui.util.ButtonWithIcon


@Composable
fun LocationDetailInfo(
    searchResult: NodeInfo,
    modifier: Modifier = Modifier,
    onDepart: () -> Unit,
    onArrival: () -> Unit
) {
    val pages = listOf("시설 정보", "리뷰", "사진")
    var tabState by remember { mutableIntStateOf(0) }

    Column {
        LocationDetailHeader(
            searchResult,
            onDepart = onDepart,
            onArrival = onArrival
        )
        Spacer(Modifier.height(16.dp))
        ScrollableTabRow (
            // 1. 현재 선택된 탭의 인덱스
            selectedTabIndex = tabState,
            containerColor = White,
            contentColor = Black,
            edgePadding = 0.dp, // 스크롤 시 좌우 끝에 생기는 여백 제거

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
                            modifier = Modifier.wrapContentSize(),
                            color = if (tabState == index) Black else Gray500
                        )
                    },
                    selectedContentColor = Black,
                    unselectedContentColor = Gray500,
                )
            }
        }
        HorizontalDivider(Modifier.height(1.dp), color = Gray500)
        LazyColumn(
            modifier = modifier
                .fillMaxWidth()
                .weight(1f),
        ) {
            items(places) { place ->
                RecommendPlaceItem(place)
            }
        }
    }
}

@Composable
fun LocationDetailHeader(
    searchResult: NodeInfo,
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
                text = searchResult.name ?: "",
                style = AppTypography.Bold_22.copy(color = Black)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painterResource(
                        id = R.drawable.ic_bookmark_true
                        //if (place.isBookmarked) R.drawable.ic_bookmark_true else R.drawable.ic_bookmark_false,
                    ),
                    contentDescription = "",
                    modifier = Modifier
                        .clickable {
                            //place.isBookmarked = !place.isBookmarked
                        }
                )
                Spacer(Modifier.width(10.dp))
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
            text = "인문사회관 A동 3층",
            style = AppTypography.Medium_13.copy(color = Gray600)
        )
        Spacer(Modifier.height(4.dp))
        Row {
            Text(
                text = "영업중",
                style = AppTypography.Bold_13.copy(color = PrimaryMid)
            )
            Spacer(Modifier.width(3.dp))
            Text(
                text = "21:00까지",
                style = AppTypography.Medium_13.copy(color = Gray600)
            )
        }
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