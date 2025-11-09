package com.hongildong.map.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.hongildong.map.R
import com.hongildong.map.data.entity.BookmarkFolder
import com.hongildong.map.ui.bookmark.BookmarkViewModel
import com.hongildong.map.ui.theme.AppTypography
import com.hongildong.map.ui.theme.Black
import com.hongildong.map.ui.theme.Gray400
import com.hongildong.map.ui.theme.Gray500
import com.hongildong.map.ui.util.BookmarkIcon
import com.hongildong.map.ui.util.FlexibleBottomSheet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookmarkScreen(
    onSearch: () -> Unit
) {
    val viewmodel: BookmarkViewModel = hiltViewModel()
    val sheetScaffoldState = rememberBottomSheetScaffoldState()

    val allBookmarkInfo by viewmodel.allBookmarkInfo.collectAsState()

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
                    addFolder = {}
                )
                BookmarkFolderList(allBookmarkInfo)
            }
        }
    }
}

@Composable
fun BookmarkFolderHeader(
    numOfFolder: Int,
    addFolder: () -> Unit
) {
    Column {
        Text("리스트 $numOfFolder", style = AppTypography.Bold_20)
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
        HorizontalDivider(thickness = 1.dp, color = Gray400)
    }
}

@Composable
fun BookmarkFolderList(
    folders: List<BookmarkFolder>
) {
    LazyColumn {
        items(folders) { folder ->
            BookmarkFolderItem(
                folderColor = folder.color,
                folderName = folder.folderName,
                numOfItem = folder.bookmarkCount
            )
        }
    }
}

@Composable
fun BookmarkFolderItem(
    folderColor: Color,
    folderName: String,
    numOfItem: Int
) {
    Column (
        modifier = Modifier.fillMaxWidth().padding(8.dp)
    ) {
        Row (
            verticalAlignment = Alignment.CenterVertically
        ) {
            BookmarkIcon(
                19.dp,
                folderColor
            )
            Spacer(Modifier.width(10.dp))
            Column (
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    folderName,
                    style = AppTypography.Medium_18.copy(color = Black)
                )
                Spacer(Modifier.height(3.dp))
                Row {
                    Image(
                        painter = painterResource(R.drawable.ic_nearby),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(Gray500),
                        modifier = Modifier
                            .size(14.dp)
                    )
                    Spacer(Modifier.width(3.dp))
                    Text(
                        "$numOfItem",
                        style = AppTypography.Medium_13.copy(Gray500)
                    )
                }
            }
            Image(
                painter = painterResource(R.drawable.ic_dot_menu),
                contentDescription = null,
                colorFilter = ColorFilter.tint(Gray500),
                modifier = Modifier
            )
        }
    }
}