package com.hongildong.map.ui.search.location_detail.facility

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.hongildong.map.R
import com.hongildong.map.data.entity.FacilityInfo
import com.hongildong.map.ui.bookmark.BookmarkViewModel
import com.hongildong.map.ui.home.RecommendPlaceItem
import com.hongildong.map.ui.home.places
import com.hongildong.map.ui.search.SearchKeywordViewmodel
import com.hongildong.map.ui.theme.AppTypography
import com.hongildong.map.ui.theme.Black
import com.hongildong.map.ui.theme.Gray500
import com.hongildong.map.ui.theme.Gray600
import com.hongildong.map.ui.theme.PrimaryMid
import com.hongildong.map.ui.theme.White
import com.hongildong.map.ui.util.ButtonWithIcon

@Composable
fun FacilityDetailInfo(
    modifier: Modifier = Modifier,
    facilityInfo: FacilityInfo,
    onDepart: () -> Unit,
    onArrival: () -> Unit,
    onBookmarkChange: () -> Unit,
    searchViewmodel: SearchKeywordViewmodel = hiltViewModel(),
    bookmarkViewmodel: BookmarkViewModel = hiltViewModel(),
) {
    val pages = listOf("시설 정보", "리뷰", "사진")
    var tabState by remember { mutableIntStateOf(0) }

    val isUser by bookmarkViewmodel.isUser.collectAsState()

    Column {
        FacilityDetailHeader(
            facilityInfo,
            onDepart = onDepart,
            onArrival = onArrival,
            onBookmarkChange = onBookmarkChange
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
        HorizontalDivider(Modifier.height(1.dp), color = Gray500)
        when (tabState) {
            0 -> {
                // 시설 정보 탭
                FacilityInfoTab(facilityInfo)
            }
            1 -> {
                // 리뷰 탭
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
            2 -> {
                // 사진 탭
                FacilityPhotoTab(
                    searchViewmodel = searchViewmodel,
                    isUser = isUser,
                    onUpdate = {
                        searchViewmodel.getFacilityPhotos(facilityInfo.id)
                    }
                )
            }
            else -> {
                // 시설 정보 탭
                FacilityInfoTab(facilityInfo)
            }
        }

    }
}

@Composable
fun FacilityDetailHeader(
    searchResult: FacilityInfo,
    onDepart: () -> Unit,
    onArrival: () -> Unit,
    onBookmarkChange: () -> Unit
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
                        if (searchResult.isBookmarked) R.drawable.ic_bookmark_true else R.drawable.ic_bookmark_false,
                    ),
                    contentDescription = "",
                    modifier = Modifier
                        .clickable {
                            onBookmarkChange()
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
            text = searchResult.nodeName,
            style = AppTypography.Medium_13.copy(color = Gray600)
        )
        Spacer(Modifier.height(4.dp))
        Row {
            Text(
                text = searchResult.open ?: "영업중",
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