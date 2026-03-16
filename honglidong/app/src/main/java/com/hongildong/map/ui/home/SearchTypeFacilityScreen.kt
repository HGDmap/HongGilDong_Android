package com.hongildong.map.ui.home

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.hongildong.map.data.entity.NodeInfo
import com.hongildong.map.data.entity.SearchableNodeType
import com.hongildong.map.data.entity.toNodeInfo
import com.hongildong.map.ui.bookmark.BookmarkViewModel
import com.hongildong.map.ui.bookmark.sheet_content.BookmarkFolderUpdateContent
import com.hongildong.map.ui.bookmark.sheet_content.BookmarkUpdateContent
import com.hongildong.map.ui.search.EmptyItem
import com.hongildong.map.ui.search.SearchedPlaces
import com.hongildong.map.ui.util.SearchBarWithGoBack
import com.hongildong.map.ui.util.bottomsheet.BottomSheetViewModel
import com.hongildong.map.ui.util.bottomsheet.FlexibleBottomSheet
import com.hongildong.map.ui.util.map.MapViewmodel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchTypeFacilityScreen(
    bottomSheetViewModel: BottomSheetViewModel,
    bookmarkViewModel: BookmarkViewModel,
    mainViewmodel: MainViewmodel,
    mapViewModel: MapViewmodel,
    searchedWord: String,
    onClickItem: (NodeInfo) -> Unit,
    //onDirectItem: (NodeInfo) -> Unit,
    onGoBack: () -> Unit
) {
    // 시스템 뒤로가기 버튼 - 커스텀 동작과 연결
    BackHandler {
        onGoBack()
    }

    val sheetScaffoldState = rememberBottomSheetScaffoldState()
    val nestedScrollConnection = rememberNestedScrollInteropConnection()

    val isUser by mainViewmodel.isUser.collectAsState()
    val facilities by mainViewmodel.facilityByTypeInfo.collectAsState()
    val allBookmarks by bookmarkViewModel.allBookmarkInfo.collectAsState()

    LaunchedEffect(Unit) {
        mainViewmodel.verifyUser()
        val type = FacilityType.fromDisplayName(searchedWord)?.apiName ?: "CAFE"
        mainViewmodel.getFacilityByType(type)
    }

    LaunchedEffect(isUser) {
        bookmarkViewModel.getAllBookmarks()
    }

    LaunchedEffect(facilities) {
        mapViewModel.showSearchResult(facilities.map { it.toNodeInfo() })
    }

    Box(
        modifier = Modifier.background(Color.Transparent)
    ) {
        if (facilities.isEmpty()) {
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
                        invisibleDirect = true,
                        places = facilities.map { it.toNodeInfo() },
                        //onDirectItem = { onDirectItem(it) },
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