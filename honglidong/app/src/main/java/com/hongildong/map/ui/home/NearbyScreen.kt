package com.hongildong.map.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.hongildong.map.R
import com.hongildong.map.ui.bookmark.BookmarkViewModel
import com.hongildong.map.ui.theme.AppTypography
import com.hongildong.map.ui.theme.Black
import com.hongildong.map.ui.theme.Gray300
import com.hongildong.map.ui.theme.Gray500
import com.hongildong.map.ui.theme.Gray600
import com.hongildong.map.ui.theme.White
import com.hongildong.map.ui.util.bottomsheet.FlexibleBottomSheet
import com.hongildong.map.ui.util.map.MapViewmodel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NearbyScreen(
    onSearch: () -> Unit,
    bookmarkViewModel: BookmarkViewModel = hiltViewModel(),
    mapViewModel: MapViewmodel
) {
    val sheetScaffoldState = rememberBottomSheetScaffoldState()
    val nestedScrollConnection = rememberNestedScrollInteropConnection()

    val allBookmarks by bookmarkViewModel.allBookmarkInfo.collectAsState()
    LaunchedEffect(Unit) {
        bookmarkViewModel.getAllBookmarks()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(),
        contentAlignment = Alignment.TopCenter
    ) {
        Column {
            SearchBar(onSearch = onSearch)
            Spacer(Modifier.height(5.dp))
            FacilityTypeTags()
        }

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
                Text(stringResource(R.string.place_recommend), style = AppTypography.Bold_20)
                Spacer(Modifier.height(10.dp))
                RecommendPlaces()
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecommendPlaces(
    modifier: Modifier = Modifier
) {
    val pages = listOf("쉬기 좋은", "공부하기 좋은", "경치 좋은", "회의하기 좋은", "맛있는")
    var tabState by remember { mutableIntStateOf(0) }

    Column {
        ScrollableTabRow(
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

            // 3. 탭 하단의 기본 회색 구분선을 제거합니다.
            divider = {},
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
fun RecommendPlaceItem(
    place: Place
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),

    ) {
        Spacer(Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    place.name,
                    style = AppTypography.Medium_18.copy(color = Black)
                )
                Spacer(Modifier.height(3.dp))
                Text(
                    place.location,
                    style = AppTypography.Medium_13.copy(color = Gray600)
                )
            }
            Image(
                painterResource(
                    id = if (place.isBookmarked) R.drawable.ic_bookmark_true else R.drawable.ic_bookmark_false,
                ),
                contentDescription = "",
                modifier = Modifier
                    .clickable {
                        place.isBookmarked = !place.isBookmarked
                    }
            )
        }
        Spacer(Modifier.height(12.dp))
        LazyRow (
            modifier = Modifier
                .height(120.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(place.images) { image ->
                Image(
                    painterResource(image ?: R.drawable.img_blank),
                    contentDescription = "",
                    modifier = Modifier
                        .size(width = 110.dp, height = 90.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
            }
        }
        //Spacer(Modifier.height(8.dp))
        HorizontalDivider(Modifier.height(1.dp), color = Gray300)
    }
}

data class Place(
    val name: String = "멀티미디어실",
    val location: String = "제4공학관 T동 605호",
    var isBookmarked: Boolean = false,
    val images: List<Int> = listOf(R.drawable.img_blank)
)

val places = listOf<Place>(
    Place(
        name = "도서관",
        location = "본관 H동 2,3층",
        isBookmarked = true,
        images = listOf(R.drawable.img_blank, R.drawable.img_blank, R.drawable.img_blank, R.drawable.img_blank)
    ),
    Place(
        name = "도서관",
        location = "본관 H동 2,3층",
        isBookmarked = false,
        images = listOf(R.drawable.img_blank,)
    ),
    Place(
        name = "도서관",
        location = "본관 H동 2,3층",
        isBookmarked = true,
        images = listOf(R.drawable.img_blank, R.drawable.img_blank)
    ),
    Place(
        name = "도서관",
        location = "본관 H동 2,3층",
        isBookmarked = false,
        images = listOf(R.drawable.img_blank, R.drawable.img_blank, R.drawable.img_blank)
    ),
)