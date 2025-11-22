package com.hongildong.map.ui.bookmark

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import com.hongildong.map.data.entity.BookmarkInfo

@Composable
fun BookmarkFolderInsideList(
    bookmarks: List<BookmarkInfo>,
    onClickBookmark: (BookmarkInfo) -> Unit,
    onModifyBookmark: (BookmarkInfo) -> Unit
) {
    LazyColumn() {
        items(bookmarks) { item ->
            BookmarkFolderInsideItem(
                bookmark = item,
                onClickBookmark = {
                    onClickBookmark(item)
                },
                onModifyBookmark = {
                    onModifyBookmark(item)
                }
            )
        }
    }
}