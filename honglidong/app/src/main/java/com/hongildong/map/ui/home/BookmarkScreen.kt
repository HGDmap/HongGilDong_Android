package com.hongildong.map.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.hongildong.map.ui.theme.AppTypography
import com.hongildong.map.ui.util.FlexibleBottomSheet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookmarkScreen(
    onSearch: () -> Unit
) {

    val sheetScaffoldState = rememberBottomSheetScaffoldState()

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
                Text("리스트 1", style = AppTypography.Bold_20)
                Spacer(Modifier.height(20.dp))
                BookmarkList()
            }
        }
    }
}

@Composable
fun BookmarkList() {
    Text("bookmark list", style = AppTypography.Regular_18)
}