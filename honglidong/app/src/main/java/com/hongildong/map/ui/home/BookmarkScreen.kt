package com.hongildong.map.ui.home

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.hongildong.map.ui.bookmark.BookmarkFolderHeader
import com.hongildong.map.ui.bookmark.BookmarkFolderList
import com.hongildong.map.ui.bookmark.BookmarkFolderUpdateContent
import com.hongildong.map.ui.bookmark.BookmarkViewModel
import com.hongildong.map.ui.util.bottomsheet.BottomSheetViewModel
import com.hongildong.map.ui.util.bottomsheet.FlexibleBottomSheet
import com.hongildong.map.ui.util.popup.ConfirmPopup

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookmarkScreen(
    onSearch: () -> Unit,
    bottomSheetViewModel: BottomSheetViewModel,
    bookmarkViewModel: BookmarkViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val sheetScaffoldState = rememberBottomSheetScaffoldState()

    val isUser by bookmarkViewModel.isUser.collectAsState()
    val allBookmarkInfo by bookmarkViewModel.allBookmarkInfo.collectAsState()
    
    var showDialog by remember { mutableStateOf(false) }
    var selectedFolderId by remember { mutableIntStateOf(0) }

    LaunchedEffect(isUser) {
        bookmarkViewModel.verifyUser()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(),
        contentAlignment = Alignment.TopCenter
    ) {
        Column {
            SearchBar(onSearch)
            Spacer(Modifier.height(5.dp))
            FacilityTypeTags()
        }

        FlexibleBottomSheet(
            sheetScaffoldState = sheetScaffoldState,
            isFullscreen = false
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top
            ) {
                BookmarkFolderHeader(
                    numOfFolder = allBookmarkInfo.size,
                    addFolder = {
                        if (isUser) {
                            bottomSheetViewModel.show(
                                {
                                    BookmarkFolderUpdateContent(
                                        onDone = {
                                            bookmarkViewModel.addFolder(it.folderName, it.folderColor)
                                            bottomSheetViewModel.hide()
                                        }
                                    )
                                }
                            )
                        } else {
                            Toast.makeText(context, "북마크 기능을 사용하려면 로그인이 필요합니다.", Toast.LENGTH_SHORT).show()
                        }
                    }
                )
                BookmarkFolderList(
                    allBookmarkInfo,
                    onClickFolder = { folder ->
                        // todo: 폴더 클릭시 폴더에 속한 아이템 리스트 api + 화면 전환
                    },
                    onDeleteFolder = { folderId ->
                        showDialog = true
                        selectedFolderId = folderId
                    },
                    onEditFolder = { folder ->
                        bottomSheetViewModel.show(
                            {
                                BookmarkFolderUpdateContent(
                                    initialName = folder.folderName,
                                    initialColor = folder.color,
                                    onDone = {
                                        bookmarkViewModel.updateFolder(
                                            folderId = folder.folderId,
                                            folderName = it.folderName,
                                            folderColor = it.folderColor
                                        )
                                    }
                                )
                            }
                        )
                    },
                )
            }
        }

        // 북마크 삭제 팝업
        if (showDialog) {
            ConfirmPopup(
                message = "이 폴더를 삭제합니다.",
                dismissMsg = "취소",
                confirmMsg = "삭제",
                onDismissRequest = {
                    // 삭제 취소
                    showDialog = false
                },
                onConfirmation = {
                    // 삭제
                    bookmarkViewModel.deleteFolder(selectedFolderId)
                    showDialog = false
                }
            )
        }
    }
}

