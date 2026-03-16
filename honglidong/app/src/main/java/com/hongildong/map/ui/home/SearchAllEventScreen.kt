package com.hongildong.map.ui.home

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.unit.dp
import com.hongildong.map.R
import com.hongildong.map.data.entity.EventBriefInfo
import com.hongildong.map.data.entity.toNodeInfo
import com.hongildong.map.ui.search.location_detail.facility.IconWithText
import com.hongildong.map.ui.theme.AppTypography
import com.hongildong.map.ui.theme.Black
import com.hongildong.map.ui.theme.Gray300
import com.hongildong.map.ui.theme.Gray600
import com.hongildong.map.ui.util.EmptyContents
import com.hongildong.map.ui.util.NetworkImage
import com.hongildong.map.ui.util.SearchBarWithGoBack
import com.hongildong.map.ui.util.bottomsheet.FlexibleBottomSheet
import com.hongildong.map.ui.util.formatDate
import com.hongildong.map.ui.util.map.MapViewmodel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchAllEventScreen(
    mainViewmodel: MainViewmodel,
    mapViewModel: MapViewmodel,
    searchedWord: String,
    onClickItem: (EventBriefInfo) -> Unit,
    onGoBack: () -> Unit
) {
    // 시스템 뒤로가기 버튼 - 커스텀 동작과 연결
    BackHandler {
        onGoBack()
    }

    val sheetScaffoldState = rememberBottomSheetScaffoldState()
    val nestedScrollConnection = rememberNestedScrollInteropConnection()

    val allEvent by mainViewmodel.allEventInfo.collectAsState()

    LaunchedEffect(Unit) {
        mainViewmodel.getAllEvents()
        //searchViewmodel.onSearchRawWord(searchedWord)
    }

    LaunchedEffect(allEvent) {
        mapViewModel.showSearchResult(allEvent.map { it.toNodeInfo() })
    }

    Box(
        modifier = Modifier.background(Color.Transparent)
    ) {
        if (allEvent.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                SearchBarWithGoBack(
                    searchedWord = searchedWord,
                    onGoBack = {
                        onGoBack()
                    }
                )
                EmptyContents("진행중인 이벤트가 없어요.")
            }
        } else {
            SearchBarWithGoBack(
                searchedWord = searchedWord,
                onGoBack = {
                    onGoBack()
                }
            )

            FlexibleBottomSheet(
                modifier = Modifier
                    .nestedScroll(
                        nestedScrollConnection
                    ),
                sheetScaffoldState = sheetScaffoldState,
                isFullscreen = false
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Top
                ) {
                    SearchedEvents(
                        events = allEvent,
                        onClickItem = { onClickItem(it)},
                    )
                }
            }
        }
    }
}
@Composable
fun SearchedEvents(
    events: List<EventBriefInfo>,
    onClickItem: (EventBriefInfo) -> Unit,
) {
    LazyColumn() {
        items(events) { event ->
            EventInfoItem(
                info = event,
                onClick = {
                    onClickItem(event)
                }
            )
        }
    }
}

@Composable
fun EventInfoItem(
    info: EventBriefInfo,
    onClick: () -> Unit,
) {
    Column {
        Spacer(Modifier.height(12.dp))
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    onClick()
                },
        ) {
            Column (
                modifier = Modifier.weight(1f).heightIn(100.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    info.name ?: "temp",
                    style = AppTypography.Medium_18.copy(color = Black)
                )
                Spacer(Modifier.height(8.dp))
                Column {
                    IconWithText(
                        icon = R.drawable.ic_location_info,
                        text = info.location,
                        textColor = Gray600,
                        modifier = Modifier
                    )
                    Spacer(Modifier.height(5.dp))
                    IconWithText(
                        icon = R.drawable.ic_calendar,
                        text = "${formatDate(info.eventStart)} ~ ${formatDate(info.eventEnd)}",
                        textColor = Gray600,
                        modifier = Modifier
                    )
                }
            }

            NetworkImage(
                url = info.image,
                width = 110.dp,
                height = 100.dp,
                contentDescription = null,
            )

        }
        Spacer(Modifier.height(12.dp))
        HorizontalDivider(Modifier.height(1.dp), color = Gray300)
    }

}