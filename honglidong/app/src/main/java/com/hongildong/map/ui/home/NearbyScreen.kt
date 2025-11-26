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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.hongildong.map.R
import com.hongildong.map.data.entity.FacilityInfo
import com.hongildong.map.data.entity.RecommendFacilityInfo
import com.hongildong.map.data.remote.response.RecommendPlace
import com.hongildong.map.ui.bookmark.BookmarkViewModel
import com.hongildong.map.ui.bookmark.sheet_content.BookmarkFolderUpdateContent
import com.hongildong.map.ui.bookmark.sheet_content.BookmarkUpdateContent
import com.hongildong.map.ui.search.location_detail.facility.review.FacilityRecommendType
import com.hongildong.map.ui.theme.AppTypography
import com.hongildong.map.ui.theme.Black
import com.hongildong.map.ui.theme.Gray300
import com.hongildong.map.ui.theme.Gray500
import com.hongildong.map.ui.theme.Gray600
import com.hongildong.map.ui.theme.White
import com.hongildong.map.ui.util.EmptyContents
import com.hongildong.map.ui.util.NetworkImage
import com.hongildong.map.ui.util.bottomsheet.BottomSheetViewModel
import com.hongildong.map.ui.util.bottomsheet.FlexibleBottomSheet
import com.hongildong.map.ui.util.map.MapViewmodel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NearbyScreen(
    onSearch: () -> Unit,
    onSearchFacility: (RecommendFacilityInfo) -> Unit,
    bookmarkViewModel: BookmarkViewModel = hiltViewModel(),
    mapViewModel: MapViewmodel,
    bottomSheetViewModel: BottomSheetViewModel
) {
    val mainViewmodel: MainViewmodel = hiltViewModel()
    val sheetScaffoldState = rememberBottomSheetScaffoldState()
    val nestedScrollConnection = rememberNestedScrollInteropConnection()

    val isUser by bookmarkViewModel.isUser.collectAsState()
    val allBookmarks by bookmarkViewModel.allBookmarkInfo.collectAsState()
    val recommendLocations by mainViewmodel.recommendLocations.collectAsState()

    LaunchedEffect(Unit) {
        bookmarkViewModel.verifyUser()
        mainViewmodel.getRecommendLocations()
    }
    LaunchedEffect(isUser) {
        if (isUser) {
            bookmarkViewModel.getAllBookmarks()
        }
    }
    LaunchedEffect(allBookmarks) {
        if (allBookmarks.isNotEmpty()) {
            mapViewModel.showBookmarks(allBookmarks)
        }
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
                RecommendPlaces(
                    recommendLocations = recommendLocations,
                    onClickBookmark = { facilityInfo ->
                        if (isUser) {
                            bottomSheetViewModel.show {
                                BookmarkUpdateContent(
                                    title = facilityInfo.name,
                                    addFolder = {
                                        bottomSheetViewModel.change {
                                            BookmarkFolderUpdateContent(
                                                onDone = {
                                                    bookmarkViewModel.addFolder(
                                                        it.folderName,
                                                        it.folderColor
                                                    )
                                                    bottomSheetViewModel.restore()
                                                }
                                            )
                                        }
                                    },
                                    folders = allBookmarks,
                                    onDone = { folderNumber ->
                                        val targetId = facilityInfo.id
                                        targetId?.let {
                                            if (folderNumber == 0) {
                                                // 0: 폴더 선택하지 않은 경우 -> 북마크 삭제
                                                bookmarkViewModel.deleteBookmark(
                                                    type = facilityInfo.type,
                                                    targetId = targetId
                                                )
                                            } else {
                                                // 0이 아님: 폴더를 선택하거나 바꾼 경우 -> 북마크 업데이트
                                                bookmarkViewModel.updateBookmark(
                                                    type = facilityInfo.type,
                                                    targetId = targetId,
                                                    folderId = folderNumber
                                                )
                                            }
                                        }
                                        bottomSheetViewModel.hide()
                                    }
                                )
                            }
                        }
                    },
                    onClickFacility = {
                        onSearchFacility(it)
                    }
                )
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecommendPlaces(
    recommendLocations: List<RecommendPlace> = emptyList(),
    modifier: Modifier = Modifier,
    onClickBookmark: (RecommendFacilityInfo) -> Unit,
    onClickFacility: (RecommendFacilityInfo) -> Unit
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
            var target: List<RecommendFacilityInfo> = emptyList()
            when (tabState) {
                0 -> {
                    // 쉬기 좋은
                    target = recommendLocations.find { it.hashTag == FacilityRecommendType.REST.apiName }?.facilityList ?: emptyList()
                }
                1 -> {
                    // 공부하기 좋은
                    target = recommendLocations.find { it.hashTag == FacilityRecommendType.STUDY.apiName }?.facilityList ?: emptyList()
                }
                2 -> {
                    // 경치 좋은
                    target = recommendLocations.find { it.hashTag == FacilityRecommendType.VIEW.apiName }?.facilityList ?: emptyList()
                }
                3 -> {
                    // 회의하기 좋은
                    target = recommendLocations.find { it.hashTag == FacilityRecommendType.MEETING.apiName }?.facilityList ?: emptyList()
                }
                4 -> {
                    // 맛있는
                    target = recommendLocations.find { it.hashTag == FacilityRecommendType.FOOD.apiName }?.facilityList ?: emptyList()
                }
            }

            if (target.isEmpty())  {
                item {
                    Column (
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(Modifier.height(150.dp))
                        EmptyContents("추천 시설이 없어요")
                    }
                }
            } else {
                items(target) { place ->
                    RecommendPlaceItem(
                        place = place,
                        onClickBookmark = {
                            onClickBookmark(place)
                        },
                        onClickFacility = {
                            onClickFacility(place)
                        }
                    )
                }
            }
        }
    }

}

@Composable
fun RecommendPlaceItem(
    place: RecommendFacilityInfo,
    onClickBookmark: () -> Unit = {},
    onClickFacility: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClickFacility()
            },

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
                    id = if (place.isBookmarked == true) R.drawable.ic_bookmark_true else R.drawable.ic_bookmark_false,
                ),
                contentDescription = "",
                modifier = Modifier
                    .clickable {
                        /*place.isBookmarked = !place.isBookmarked*/
                        onClickBookmark()
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
                if (!image.isNullOrEmpty()) {
                    NetworkImage(
                        url = image,
                        width = 110.dp,
                        height = 90.dp,
                        contentDescription = null,
                    )
                }
            }
        }
        //Spacer(Modifier.height(8.dp))
        HorizontalDivider(Modifier.height(1.dp), color = Gray300)
    }
}
