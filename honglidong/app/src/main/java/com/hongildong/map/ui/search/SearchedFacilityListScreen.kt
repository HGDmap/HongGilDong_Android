package com.hongildong.map.ui.search

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.hongildong.map.R
import com.hongildong.map.data.entity.NodeInfo
import com.hongildong.map.data.entity.SearchableNodeType
import com.hongildong.map.ui.bookmark.sheet_content.BookmarkFolderUpdateContent
import com.hongildong.map.ui.bookmark.sheet_content.BookmarkUpdateContent
import com.hongildong.map.ui.bookmark.BookmarkViewModel
import com.hongildong.map.ui.util.SearchBarWithGoBack
import com.hongildong.map.ui.theme.AppTypography
import com.hongildong.map.ui.theme.Black
import com.hongildong.map.ui.theme.Gray300
import com.hongildong.map.ui.theme.Gray600
import com.hongildong.map.ui.theme.White
import com.hongildong.map.ui.util.ButtonWithIcon
import com.hongildong.map.ui.util.NetworkImage
import com.hongildong.map.ui.util.bottomsheet.BottomSheetViewModel
import com.hongildong.map.ui.util.bottomsheet.FlexibleBottomSheet
import com.hongildong.map.ui.util.map.MapViewmodel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchedFacilityListScreen(
    searchViewmodel: SearchKeywordViewmodel,
    bottomSheetViewModel: BottomSheetViewModel,
    bookmarkViewModel: BookmarkViewModel,
    mapViewModel: MapViewmodel,
    searchedWord: String,
    onDirectItem: (NodeInfo) -> Unit,
    onClickItem: (NodeInfo) -> Unit,
    onGoBack: () -> Unit
) {
    // 시스템 뒤로가기 버튼 - 커스텀 동작과 연결
    BackHandler {
        onGoBack()
    }

    val searchResult by searchViewmodel.searchedList.collectAsState()

    val sheetScaffoldState = rememberBottomSheetScaffoldState()
    val nestedScrollConnection = rememberNestedScrollInteropConnection()

    val isUser by bookmarkViewModel.isUser.collectAsState()
    val allBookmarks by bookmarkViewModel.allBookmarkInfo.collectAsState()

    LaunchedEffect(Unit) {
        bookmarkViewModel.getAllBookmarks()
        //searchViewmodel.onSearchRawWord(searchedWord)
    }

    LaunchedEffect(searchResult) {
        mapViewModel.showSearchResult(searchResult)
    }

    Box(
        modifier = Modifier.background(Color.Transparent)
    ) {
        if (searchResult.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                SearchBarWithGoBack(
                    searchedWord = searchedWord,
                    onGoBack = {
                        onGoBack()
                    }
                )
                EmptyItem()
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
                    SearchedPlaces(
                        places = searchResult,
                        onDirectItem = { onDirectItem(it) },
                        onClickItem = { onClickItem(it)},
                        onBookmarkChange = {
                            var bookmarkInfo = false
                            if (isUser) {
                                bottomSheetViewModel.show {
                                    BookmarkUpdateContent(
                                        title = it.name ?: it.nodeName ?: "",
                                        addFolder = {
                                            bottomSheetViewModel.change {
                                                BookmarkFolderUpdateContent(
                                                    onDone = { request ->
                                                        bookmarkViewModel.addFolder(request.folderName, request.folderColor)
                                                        bottomSheetViewModel.restore()
                                                    }
                                                )
                                            }
                                        },
                                        folders = allBookmarks,
                                        onDone = { folderNumber ->
                                            if (folderNumber == 0) {
                                                // 0: 폴더 선택하지 않은 경우 -> 북마크 삭제
                                                if (it.type == SearchableNodeType.FACILITY.apiName) {
                                                    bookmarkViewModel.deleteBookmark(
                                                        type = it.type ?: SearchableNodeType.FACILITY.apiName,
                                                        targetId = it.id ?: it.nodeId ?: 0
                                                    )
                                                    bookmarkInfo = false
                                                }
                                            } else {
                                                // 0이 아님: 폴더를 선택하거나 바꾼 경우 -> 북마크 업데이트
                                                if (it.type == SearchableNodeType.FACILITY.apiName) {
                                                    bookmarkViewModel.updateBookmark(
                                                        type = it.type ?: SearchableNodeType.FACILITY.apiName,
                                                        targetId = it.id ?: it.nodeId ?: 0,
                                                        folderId = folderNumber
                                                    )
                                                    bookmarkInfo = true
                                                }
                                            }
                                            bottomSheetViewModel.hide()
                                        }
                                    )
                                }
                            }
                            bookmarkInfo
                        }
                    )
                }
            }
        }
    }
}


@Composable
fun EmptyItem() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painterResource(R.drawable.ic_empty_head),
            contentDescription = null,
            modifier = Modifier.size(50.dp),
            contentScale = ContentScale.Fit
        )
        Spacer(Modifier.height(8.dp))
        Text(
            "검색 결과가 없습니다.",
            style = AppTypography.Bold_20.copy(color = Black)
        )
        Spacer(Modifier.height(4.dp))
        Text(
            "찾고 있는 결과가 없다면 등록해 보세요.",
            style = AppTypography.Medium_15.copy(color = Black)
        )
    }
}

@Composable
fun SearchedPlaces(
    places: List<NodeInfo>,
    onDirectItem: (NodeInfo) -> Unit = {},
    onClickItem: (NodeInfo) -> Unit,
    onBookmarkChange: (NodeInfo) -> Boolean,
    invisibleDirect: Boolean = false
) {
    LazyColumn() {
        items(places) { place ->
            PlaceInfoItem(
                info = place,
                onDirect = {
                    onDirectItem(place)
                },
                onClick = {
                    onClickItem(place)
                },
                onBookmarkChange = {
                    onBookmarkChange(place)
                },
                invisibleDirect = invisibleDirect
            )
        }
    }
}

@Composable
fun PlaceInfoItem(
    info: NodeInfo,
    invisibleDirect: Boolean = false,
    onDirect: () -> Unit = {},
    onClick: () -> Unit,
    onBookmarkChange: () -> Boolean
) {
    var bookmarkInfo by remember (info) { mutableStateOf(info.isBookmarked ?: false) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
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
                    info.name ?: "temp",
                    style = AppTypography.Medium_18.copy(color = Black)
                )
                Spacer(Modifier.height(3.dp))
                Text(
                    info.description ?: info.name ?: "",
                    style = AppTypography.Medium_13.copy(color = Gray600)
                )
            }
            if (info.type == SearchableNodeType.FACILITY.apiName) {
                Image(
                    painterResource(
                        id = if (bookmarkInfo) R.drawable.ic_bookmark_true else R.drawable.ic_bookmark_false,
                    ),
                    contentDescription = "",
                    modifier = Modifier
                        .clickable {
                            bookmarkInfo = onBookmarkChange()
                        }
                )
            }
        }
        Spacer(Modifier.height(12.dp))
        if (info.type == SearchableNodeType.FACILITY.apiName) {
            LazyRow (
                modifier = Modifier
                    .height(120.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(info.photoList) { image ->
                    NetworkImage(
                        url = image,
                        width = 110.dp,
                        height = 90.dp,
                        contentDescription = null,
                    )
                }
            }
        } else {
            NetworkImage(
                url = info.photoList[0],
                height = 90.dp,
                contentDescription = null,
                modifier = Modifier.fillMaxWidth()
            )
        }
        if (!invisibleDirect) {
            Spacer(Modifier.height(5.dp))
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.TopEnd
            ) {
                ButtonWithIcon(
                    icon = R.drawable.ic_direction,
                    title = "길찾기",
                    onClick = { onDirect() }
                )
            }
        }

        Spacer(Modifier.height(8.dp))
        HorizontalDivider(Modifier.height(1.dp), color = Gray300)
    }
}