package com.hongildong.map.ui.search.location_detail

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.hongildong.map.R
import com.hongildong.map.data.entity.NodeInfo
import com.hongildong.map.data.entity.SearchableNodeType
import com.hongildong.map.data.entity.toSearchKeyword
import com.hongildong.map.ui.bookmark.sheet_content.BookmarkFolderUpdateContent
import com.hongildong.map.ui.bookmark.sheet_content.BookmarkUpdateContent
import com.hongildong.map.ui.bookmark.BookmarkViewModel
import com.hongildong.map.ui.search.SearchKeywordViewmodel
import com.hongildong.map.ui.theme.AppTypography
import com.hongildong.map.ui.theme.Black
import com.hongildong.map.ui.theme.Gray400
import com.hongildong.map.ui.theme.White
import com.hongildong.map.ui.util.bottomsheet.AnchoredDraggableBottomSheet
import com.hongildong.map.ui.util.bottomsheet.BottomSheetViewModel
import com.hongildong.map.ui.util.map.MapViewmodel
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.compose.ExperimentalNaverMapApi

@OptIn(ExperimentalMaterial3Api::class, ExperimentalNaverMapApi::class)
@Composable
fun LocationDetailScreen(
    searchedWord: String = "",
    searchViewmodel: SearchKeywordViewmodel,
    mapViewmodel: MapViewmodel,
    bookmarkViewModel: BookmarkViewModel,
    bottomSheetViewModel: BottomSheetViewModel,
    onGoBack: () -> Unit,
    onSearchDirection: () -> Unit,
) {
    val context = LocalContext.current
    val searchResult by searchViewmodel.searchResult.collectAsState()
    val directionResult by searchViewmodel.directionResult.collectAsState()
    val isUser by bookmarkViewModel.isUser.collectAsState()
    val allBookmarks by bookmarkViewModel.allBookmarkInfo.collectAsState()

    LaunchedEffect(key1 = searchResult, key2 = directionResult) {
        // searchResult가 null이 아닐 때만 실행
        searchResult?.let { result ->
            val newPosition = LatLng(result.latitude, result.longitude)
            mapViewmodel.showMarkerAndMoveCamera(newPosition, result.name ?: "")
        }

        directionResult?.let { response ->
            mapViewmodel.showPath(directionResult!!.nodes)
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
                LocationDetailInfo(
                    modifier = Modifier.nestedScroll(nestedScrollConnection),
                    searchResult = searchResult ?: NodeInfo(0.0,0.0,"temp", "", "",0, nodeId = 0),
                    onDepart = {
                        if (searchResult != null) {
                            // 검색 결과를 바탕으로 출발지 설정
                            val keyword = searchResult!!.toSearchKeyword()

                            // 출발지 설정
                            searchViewmodel.setDepart(keyword)
                            // 경로 검색 화면으로 화면 전환
                            onSearchDirection()
                        }
                        else Toast.makeText(context, "유효하지 않은 장소입니다.", Toast.LENGTH_SHORT).show()

                        /*mapViewmodel.clearMarker()
                        searchViewmodel.direct()*/
                    },
                    onArrival = {
                        if (searchResult != null) {
                            // 검색 결과를 바탕으로 도착지 설정
                            val keyword = searchResult!!.toSearchKeyword()

                            // 도착지 설정
                            searchViewmodel.setArrival(keyword)
                            // 경로 검색 화면으로 화면 전환
                            onSearchDirection()
                        }
                        else Toast.makeText(context, "유효하지 않은 장소입니다.", Toast.LENGTH_SHORT).show()
                    },
                    onBookmarkChange = {
                        if (isUser) {
                            bottomSheetViewModel.show {
                                BookmarkUpdateContent(
                                    title = searchResult?.name ?: searchResult?.nodeName ?: "",
                                    addFolder = {
                                        bottomSheetViewModel.change {
                                            BookmarkFolderUpdateContent(
                                                onDone = {
                                                    bookmarkViewModel.addFolder(it.folderName, it.folderColor)
                                                    bottomSheetViewModel.restore()
                                                }
                                            )
                                        }
                                    },
                                    folders = allBookmarks,
                                    onDone = { folderNumber ->
                                        val targetId = if (searchResult?.type == SearchableNodeType.FACILITY.apiName) {
                                            searchResult?.id
                                        } else {
                                            searchResult?.nodeId
                                        }
                                        targetId?.let {
                                            if (folderNumber == 0) {
                                                // 0: 폴더 선택하지 않은 경우 -> 북마크 삭제
                                                bookmarkViewModel.deleteBookmark(
                                                    type = searchResult?.type ?: SearchableNodeType.FACILITY.apiName,
                                                    targetId = targetId
                                                )
                                            }
                                            else {
                                                // 0이 아님: 폴더를 선택하거나 바꾼 경우 -> 북마크 업데이트
                                                bookmarkViewModel.updateBookmark(
                                                    type = searchResult?.type ?: SearchableNodeType.FACILITY.apiName,
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
                    }
                )
            }
        }
    }
}


@Composable
fun SearchBarWithGoBack(
    searchedWord: String,
    onGoBack: () -> Unit
) {
    Row(
        modifier = Modifier
            .shadow(3.dp)
            .background(White)
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.statusBars)
            .padding(top = 16.dp, start = 10.dp, end = 10.dp, bottom = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        Image(
            painter = painterResource(R.drawable.ic_back),
            contentDescription = stringResource(R.string.go_back),
            modifier = Modifier
                .clickable {
                    onGoBack()
                }
        )
        Spacer(
            modifier = Modifier.width(15.dp)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(53.dp)
                .background(color = White, shape = RoundedCornerShape(size = 10.dp))
                .border(1.dp, color = Gray400, shape = RoundedCornerShape(size = 10.dp)),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = searchedWord,
                color = Black,
                style = AppTypography.Regular_15,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
    }
}