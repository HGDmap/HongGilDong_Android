package com.hongildong.map.ui.home

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.hongildong.map.ui.bookmark.BookmarkFolderHeader
import com.hongildong.map.ui.bookmark.BookmarkFolderList
import com.hongildong.map.ui.bookmark.BookmarkFolderUpdateContent
import com.hongildong.map.ui.bookmark.BookmarkViewModel
import com.hongildong.map.ui.util.bottomsheet.BottomSheetViewModel
import com.hongildong.map.ui.util.bottomsheet.FlexibleBottomSheet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookmarkScreen(
    onSearch: () -> Unit,
    bottomSheetViewModel: BottomSheetViewModel
) {
    val bookMarkViewmodel: BookmarkViewModel = hiltViewModel()
    val sheetScaffoldState = rememberBottomSheetScaffoldState()

    val allBookmarkInfo by bookMarkViewmodel.allBookmarkInfo.collectAsState()

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
                        bottomSheetViewModel.show(
                            {
                                BookmarkFolderUpdateContent(
                                    onClose = {
                                        bottomSheetViewModel.hide()
                                    }
                                )
                            }
                        )
                    }
                )
                BookmarkFolderList(allBookmarkInfo)
            }
        }
    }
}

