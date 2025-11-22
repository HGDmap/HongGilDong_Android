package com.hongildong.map.ui.bookmark.sheet_content

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.hongildong.map.R
import com.hongildong.map.data.entity.BookmarkFolder
import com.hongildong.map.data.entity.FolderColor
import com.hongildong.map.ui.bookmark.SelectableBookmarkFolderItem
import com.hongildong.map.ui.theme.AppTypography
import com.hongildong.map.ui.theme.BookmarkRed
import com.hongildong.map.ui.theme.Gray300
import com.hongildong.map.ui.theme.Gray500
import com.hongildong.map.ui.util.BottomButton


@Composable
fun BookmarkUpdateContent(
    title: String,
    addFolder: () -> Unit,
    folders: List<BookmarkFolder>,
    onDone: (Int) -> Unit
) {
    var selectedFolder by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(title, style = AppTypography.Bold_20)
        Spacer(Modifier.height(20.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    addFolder()
                }
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_add_folder),
                contentDescription = null,
                tint = Gray500,
                modifier = Modifier.padding(8.dp)
            )
            Text("새 리스트 만들기", style = AppTypography.Medium_18.copy(color = Gray500))
        }
        Spacer(Modifier.height(4.dp))
        HorizontalDivider(thickness = 1.dp, color = Gray300)

        LazyColumn {
            items(folders) { folder ->
                SelectableBookmarkFolderItem(
                    folderColor = FolderColor.fromColorName(folder.color)?.color ?: BookmarkRed,
                    folderName = folder.folderName,
                    numOfItem = folder.bookmarkCount,
                    onClick = {
                        if (selectedFolder == folder.folderId) {
                            selectedFolder = 0
                        } else {
                            selectedFolder = folder.folderId
                        }
                    },
                    isSelected = selectedFolder == folder.folderId
                )
            }
        }

        BottomButton(
            buttonText = if (selectedFolder == 0) "북마크 취소" else "저장",
            isButtonEnabled = true,
            onClick = {
                onDone(selectedFolder)
                selectedFolder = 0
            }
        )
    }
}

