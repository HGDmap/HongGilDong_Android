package com.hongildong.map.ui.search.location_detail.facility

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.hongildong.map.data.entity.ReviewInfo
import com.hongildong.map.data.entity.SearchableNodeType
import com.hongildong.map.data.entity.toSearchKeyword
import com.hongildong.map.ui.bookmark.sheet_content.BookmarkFolderUpdateContent
import com.hongildong.map.ui.bookmark.sheet_content.BookmarkUpdateContent
import com.hongildong.map.ui.bookmark.BookmarkViewModel
import com.hongildong.map.ui.search.SearchKeywordViewmodel
import com.hongildong.map.ui.search.location_detail.SearchBarWithGoBack
import com.hongildong.map.ui.util.bottomsheet.AnchoredDraggableBottomSheet
import com.hongildong.map.ui.util.bottomsheet.BottomSheetViewModel
import com.hongildong.map.ui.util.map.MapViewmodel
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.compose.ExperimentalNaverMapApi

@OptIn(ExperimentalMaterial3Api::class, ExperimentalNaverMapApi::class)
@Composable
fun FacilityDetailScreen(
    searchedFacilityId: Int = 0,
    searchedWord: String = "",
    searchViewmodel: SearchKeywordViewmodel,
    mapViewmodel: MapViewmodel,
    bookmarkViewModel: BookmarkViewModel,
    bottomSheetViewModel: BottomSheetViewModel,
    onGoBack: () -> Unit,
    onSearchDirection: () -> Unit,
    onReview: (FacilityInfo) -> Unit,
    onEditReview: (Int, ReviewInfo) -> Unit,
    onDeleteReview: (Int) -> Unit
) {
    val context = LocalContext.current
    val facilityInfo by searchViewmodel.facilityDetail.collectAsState()
    val directionResult by searchViewmodel.directionResult.collectAsState()
    val isUser by bookmarkViewModel.isUser.collectAsState()
    val allBookmarks by bookmarkViewModel.allBookmarkInfo.collectAsState()

    LaunchedEffect(Unit) {
        // 검색 결과 바탕으로 시설 상세 정보 api 호출
        searchViewmodel.onSearchFacilityInfo(searchedFacilityId)
        bookmarkViewModel.getAllBookmarks()
    }

    LaunchedEffect(key1 = facilityInfo, key2 = directionResult) {
        bookmarkViewModel.verifyUser()

        // facilityInfo가 null이 아닐 때만 실행
        facilityInfo?.let { result ->
            val newPosition = LatLng(result.latitude, result.longitude)
            mapViewmodel.showMarkerAndMoveCamera(newPosition, result.name)
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
                FacilityDetailInfo(
                    modifier = Modifier.nestedScroll(nestedScrollConnection),
                    searchViewmodel = searchViewmodel,
                    bookmarkViewmodel = bookmarkViewModel,
                    facilityInfo = facilityInfo ?: FacilityInfo(
                        "임시 데이터",
                        "임시 시설",
                        0,
                        false,
                        0.0,
                        "",
                        0.0,
                        0,
                        "temp",
                        null,
                        "",
                        emptyList(),
                        ""
                    ),
                    onDepart = {
                        if (facilityInfo != null) {
                            // 검색 결과를 바탕으로 출발지 설정
                            val keyword = facilityInfo!!.toSearchKeyword()

                            // 출발지 설정
                            searchViewmodel.setDepart(keyword)
                            // 경로 검색 화면으로 화면 전환
                            onSearchDirection()
                        } else Toast.makeText(context, "유효하지 않은 장소입니다.", Toast.LENGTH_SHORT).show()
                    },
                    onArrival = {
                        if (facilityInfo != null) {
                            // 검색 결과를 바탕으로 도착지 설정
                            val keyword = facilityInfo!!.toSearchKeyword()

                            // 도착지 설정
                            searchViewmodel.setArrival(keyword)
                            // 경로 검색 화면으로 화면 전환
                            onSearchDirection()
                        } else Toast.makeText(context, "유효하지 않은 장소입니다.", Toast.LENGTH_SHORT).show()
                    },
                    onBookmarkChange = {
                        if (isUser && (facilityInfo != null)) {
                            bottomSheetViewModel.show {
                                BookmarkUpdateContent(
                                    title = facilityInfo?.name ?: "",
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
                                        val targetId = facilityInfo?.id
                                        targetId?.let {
                                            if (folderNumber == 0) {
                                                // 0: 폴더 선택하지 않은 경우 -> 북마크 삭제
                                                bookmarkViewModel.deleteBookmark(
                                                    type = facilityInfo?.type
                                                        ?: SearchableNodeType.FACILITY.apiName,
                                                    targetId = targetId
                                                )
                                            } else {
                                                // 0이 아님: 폴더를 선택하거나 바꾼 경우 -> 북마크 업데이트
                                                bookmarkViewModel.updateBookmark(
                                                    type = facilityInfo?.type
                                                        ?: SearchableNodeType.FACILITY.apiName,
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
                    onReview = {
                        if (facilityInfo == null) {
                            Toast.makeText(context, "유효하지 않은 장소입니다.", Toast.LENGTH_SHORT).show()
                        } else {
                            onReview(facilityInfo!!)
                        }
                    },
                    onEditReview = {
                        if (facilityInfo == null) {
                            Toast.makeText(context, "유효하지 않은 장소입니다.", Toast.LENGTH_SHORT).show()
                        } else {
                            onEditReview(facilityInfo!!.id, it)
                        }
                    },
                    onDeleteReview = {
                        if (facilityInfo != null) {
                            onDeleteReview(it)
                        }
                    },
                )
            }
        }
    }
}
