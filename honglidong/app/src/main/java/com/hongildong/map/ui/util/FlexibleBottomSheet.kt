package com.hongildong.map.ui.util

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.hongildong.map.ui.theme.Gray300
import com.hongildong.map.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlexibleBottomSheet(
    modifier: Modifier = Modifier,
    sheetScaffoldState: BottomSheetScaffoldState,
    enableSwipe: Boolean = true,
    content: @Composable (() -> Unit)
) {
    BottomSheetScaffold(
        modifier = modifier,
        sheetShape = RoundedCornerShape(topStart = 22.dp, topEnd = 22.dp),
        scaffoldState = sheetScaffoldState,
        sheetContainerColor = White,
        sheetContentColor = White,
        sheetTonalElevation = 6.dp,
        sheetSwipeEnabled = enableSwipe,
        sheetDragHandle = {
            BottomSheetDefaults.DragHandle(
                color = Gray300,
                width = 60.dp
            )
        },
        sheetContent = {
            Column(
                modifier = Modifier.fillMaxWidth().fillMaxHeight(0.4f).padding(horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                content()
            }
        },
        sheetPeekHeight = 40.dp,
    ) {
        Box(
            modifier = Modifier.background(Color.Transparent),
        ) {
        }
    }
}