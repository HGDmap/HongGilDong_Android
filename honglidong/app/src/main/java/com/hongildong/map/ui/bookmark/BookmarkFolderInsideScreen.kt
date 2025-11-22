package com.hongildong.map.ui.bookmark

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.hongildong.map.R
import com.hongildong.map.data.entity.BookmarkInfo
import com.hongildong.map.data.entity.FolderColor
import com.hongildong.map.data.entity.SearchableNodeType
import com.hongildong.map.ui.bookmark.sheet_content.BookmarkFolderUpdateContent
import com.hongildong.map.ui.bookmark.sheet_content.BookmarkUpdateContent
import com.hongildong.map.ui.theme.AppTypography
import com.hongildong.map.ui.theme.Black
import com.hongildong.map.ui.theme.BookmarkMint
import com.hongildong.map.ui.theme.Gray600
import com.hongildong.map.ui.theme.White
import com.hongildong.map.ui.util.BookmarkIcon
import com.hongildong.map.ui.util.bottomsheet.BottomSheetViewModel

@Composable
fun BookmarkFolderInsideScreen(
    folderId: Int,
    bookmarkViewModel: BookmarkViewModel,
    bottomSheetViewModel: BottomSheetViewModel,
    onClickBookmark: (BookmarkInfo) -> Unit,
    onGoBack: () -> Unit
) {
    val allFolderInfo by bookmarkViewModel.allBookmarkInfo.collectAsState()
    val folderData by bookmarkViewModel.folderInfo.collectAsState()

    LaunchedEffect(Unit) {
        bookmarkViewModel.getBookmarksOfFolder(folderId)
    }

    Column(
        modifier = Modifier
            .background(White)
            .systemBarsPadding()
            .fillMaxSize()
            .padding(horizontal = 20.dp)
    ) {
        BookmarkFolderInsideHeader(
            title = folderData?.folderName ?: "temp folder",
            iconColor = FolderColor.fromColorName(folderData?.color ?: "mint")?.color ?: BookmarkMint,
            onGoBack = onGoBack
        )
        BookmarkFolderInsideList(
            bookmarks = folderData?.bookmarkList ?: emptyList(),
            onClickBookmark = {
                onClickBookmark(it)
            },
            onModifyBookmark = {
                bottomSheetViewModel.show {
                    BookmarkUpdateContent(
                        title = it.name,
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
                        folders = allFolderInfo,
                        onDone = { folderNumber ->
                            if (folderNumber == 0) {
                                // 0: 폴더 선택하지 않은 경우 -> 북마크 삭제
                                bookmarkViewModel.deleteBookmark(
                                    type = it.type,
                                    targetId = it.id
                                )
                            } else {
                                // 0이 아님: 폴더를 선택하거나 바꾼 경우 -> 북마크 업데이트
                                bookmarkViewModel.updateBookmark(
                                    type = it.type,
                                    targetId = it.id,
                                    folderId = folderNumber
                                )
                            }
                            bottomSheetViewModel.hide()
                        }
                    )
                }
            }
        )
    }
}

@Composable
fun BookmarkFolderInsideHeader(
    title: String,
    iconColor: Color,
    onGoBack: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BookmarkIcon(
                size = 20.dp,
                color = iconColor
            )
            Text(
                title,
                style = AppTypography.Bold_20.copy(color = Black)
            )
        }
        Image(
            painter = painterResource(R.drawable.ic_close),
            contentDescription = "닫기",
            colorFilter = ColorFilter.tint(Gray600),
            modifier = Modifier
                .size(20.dp)
                .align(Alignment.CenterEnd)
                .clickable {
                    onGoBack()
                }
        )
    }

}