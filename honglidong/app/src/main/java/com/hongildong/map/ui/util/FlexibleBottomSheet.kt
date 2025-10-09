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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import com.hongildong.map.ui.theme.Gray300
import com.hongildong.map.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlexibleBottomSheet(
    modifier: Modifier = Modifier,
    sheetScaffoldState: BottomSheetScaffoldState,
    isFullscreen: Boolean, // ✨ 1. 풀스크린 상태를 받는 파라미터 추가
    content: @Composable (ColumnScope.() -> Unit) // ColumnScope를 사용하도록 변경
) {
    BottomSheetScaffold(
        modifier = modifier,
        // ✨ 2. isFullscreen 상태에 따라 모양을 동적으로 변경 (둥근 모서리 -> 사각형)
        sheetShape = if (isFullscreen) RectangleShape else RoundedCornerShape(topStart = 22.dp, topEnd = 22.dp),
        scaffoldState = sheetScaffoldState,
        sheetContainerColor = White,
        sheetContentColor = White,
        sheetTonalElevation = 6.dp,

        sheetDragHandle = {
            // ✨ 3. isFullscreen 상태일 때 핸들을 숨김
            if (!isFullscreen) {
                BottomSheetDefaults.DragHandle(
                    color = Gray300,
                    width = 60.dp
                )
            }
        },
        sheetContent = {
            // ✨ 4. isFullscreen 상태에 따라 content의 Modifier를 동적으로 변경
            val contentModifier = if (isFullscreen) {
                Modifier.fillMaxSize() // '완전히 펼쳐짐' 상태
            } else {
                // 'weight 1f만큼 펼쳐짐' 상태를 화면 높이의 80%로 표현 (조절 가능)
                Modifier.fillMaxWidth().fillMaxHeight(0.8f)
            }

            Column(
                modifier = contentModifier.padding(horizontal = if (isFullscreen) 0.dp else 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                content()
            }
        },
        // 풀스크린일 때는 peekHeight가 필요 없으므로 0으로 설정
        sheetPeekHeight = if (isFullscreen) 0.dp else 40.dp,
    ) {
        // 배경 컨텐츠 (지도)
        Box(
            modifier = Modifier.background(Color.Transparent),
        ) {}
    }
}