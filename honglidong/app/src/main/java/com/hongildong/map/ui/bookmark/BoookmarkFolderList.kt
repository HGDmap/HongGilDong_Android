package com.hongildong.map.ui.bookmark

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import com.hongildong.map.data.entity.BookmarkFolder
import com.hongildong.map.data.entity.FolderColor

// 홈 - 북마크의 북마크 폴더 리스트
@Composable
fun BookmarkFolderList(
    folders: List<BookmarkFolder>,
    onClickFolder: (BookmarkFolder) -> Unit,
    onDeleteFolder: (Int) -> Unit,
    onEditFolder: (BookmarkFolder) -> Unit
) {
    LazyColumn {
        items(folders) { folder ->
            BookmarkFolderItem(
                folderColor = FolderColor.fromColorName(folder.color)!!.color,
                folderName = folder.folderName,
                numOfItem = folder.bookmarkCount,
                onClick = {
                    onClickFolder(folder)
                },
                onEditFolder = {
                    onEditFolder(folder)
                },
                onDeleteFolder = {
                    onDeleteFolder(folder.folderId)
                },
            )
        }
    }
}