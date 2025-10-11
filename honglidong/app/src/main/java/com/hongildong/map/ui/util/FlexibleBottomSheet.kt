package com.hongildong.map.ui.util

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetDefaults
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.util.VelocityTracker
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.hongildong.map.ui.theme.Gray300
import com.hongildong.map.ui.theme.White
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlexibleBottomSheet(
    modifier: Modifier = Modifier,
    sheetScaffoldState: BottomSheetScaffoldState,
    isFullscreen: Boolean,
    content: @Composable (ColumnScope.() -> Unit)
) {

    BottomSheetScaffold(
        modifier = modifier,
        sheetShape = RoundedCornerShape(topStart = 22.dp, topEnd = 22.dp),
        scaffoldState = sheetScaffoldState,
        sheetContainerColor = White,
        sheetContentColor = White,
        sheetTonalElevation = 6.dp,

        sheetDragHandle = {
            BottomSheetDefaults.DragHandle(
                color = Gray300,
                width = 60.dp
            )
        },
        sheetContent = {
            val contentModifier = if (isFullscreen) {
                Modifier.fillMaxSize() // '완전히 펼쳐짐' 상태
            } else {
                Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.8f)
            }

            Column(
                modifier = contentModifier.padding(horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                content()
            }
        },
        sheetPeekHeight = 40.dp,
    ) {
        Box(
            modifier = Modifier.background(Color.Transparent),
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FreeDragBottomSheet(
    modifier: Modifier = Modifier,
    peekHeight: Dp = 40.dp, // 2. 최저 높이를 40dp로 변경
    initialVisibleHeight: Dp = 350.dp, // 1. 초기 높이를 350dp로 설정
    content: @Composable ColumnScope.() -> Unit
) {
    val density = LocalDensity.current // dp를 px로 변환하기 위해 density를 가져옵니다.

    BoxWithConstraints(modifier = modifier) {
        val fullHeight = constraints.maxHeight.toFloat()

        // dp 단위를 px 단위로 변환합니다.
        val peekHeightPx = with(density) { peekHeight.toPx() }
        val initialVisibleHeightPx = with(density) { initialVisibleHeight.toPx() }

        // 초기 Y축 위치(offset)를 계산합니다.
        val initialOffsetY = fullHeight - initialVisibleHeightPx

        var offsetY by remember { mutableFloatStateOf(initialOffsetY) }

        Surface(
            modifier = modifier
                .fillMaxWidth()
                // 2. Animatable의 현재 값(.value)을 오프셋으로 사용합니다.
                .offset { IntOffset(0, offsetY.roundToInt()) }
                .pointerInput(Unit) {
                    detectVerticalDragGestures { change, dragAmount ->
                        change.consume()
                        // 드래그 경계를 계산합니다.
                        val maxOffsetY = fullHeight - peekHeightPx // 최저 높이 지점
                        val minOffsetY = 0f                   // 최고 높이 지점 (화면 상단)
                        offsetY = (offsetY + dragAmount).coerceIn(minOffsetY, maxOffsetY)
                    }
                },
            color = White,
            shape = RoundedCornerShape(topStart = 22.dp, topEnd = 22.dp),
            tonalElevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .padding(start = 20.dp, end=20.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                BottomSheetDefaults.DragHandle(
                    color = Gray300,
                    width = 60.dp
                )
                Spacer(Modifier.height(10.dp))
                content()
            }
        }
    }
}
