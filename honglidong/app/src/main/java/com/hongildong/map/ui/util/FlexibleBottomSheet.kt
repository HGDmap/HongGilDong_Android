package com.hongildong.map.ui.util

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.hongildong.map.ui.theme.Gray300
import com.hongildong.map.ui.theme.White
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


// 1. 바텀 시트가 가질 수 있는 3가지 상태를 명확하게 정의합니다.
enum class BottomSheetState {
    Collapsed,
    PartiallyExpanded,
    Expanded
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnchoredDraggableBottomSheet(
    modifier: Modifier = Modifier,
    isFullScreen: Boolean = false,
    maxHeight: Float,
    content: @Composable ColumnScope.() -> Unit
) {
    val density = LocalDensity.current

    val collapsedHeight = with(density) { 40.dp.toPx() }
    val partialHeight = with(density) { 300.dp.toPx() }

    val anchors = DraggableAnchors {
        BottomSheetState.Collapsed at maxHeight - collapsedHeight
        BottomSheetState.PartiallyExpanded at maxHeight - partialHeight
        BottomSheetState.Expanded at if (isFullScreen) maxHeight * 0f else maxHeight * 0.2f // 화면의 80% 높이 (상단에서 20% 지점)
    }

    val decayAnimationSpec = rememberSplineBasedDecay<Float>()
    val state = remember {
        AnchoredDraggableState(
            initialValue = BottomSheetState.PartiallyExpanded,
            anchors = anchors,
            positionalThreshold = { distance -> distance * 0.2f },
            velocityThreshold = { with(density) { 100.dp.toPx() } },
            snapAnimationSpec = spring(stiffness = Spring.StiffnessLow),
            decayAnimationSpec = decayAnimationSpec
        )
    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            // 2. state가 알려주는 현재 Y축 오프셋 값만큼 Surface를 이동시킵니다.
            .offset {
                IntOffset(
                    x = 0,
                    y = state
                        .requireOffset()
                        .roundToInt()
                )
            }
            // 3. 이 Modifier가 드래그 제스처를 감지하고 state를 업데이트하여 스냅 동작을 처리합니다.
            .anchoredDraggable(state, orientation = Orientation.Vertical),
        shape = RoundedCornerShape(topStart = 22.dp, topEnd = 22.dp),
        tonalElevation = 8.dp,
        color = White
    ) {
        Column(
            modifier = Modifier
                .padding(start = 20.dp, end = 20.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (!(isFullScreen and (state.currentValue == BottomSheetState.Expanded))) {
                BottomSheetDefaults.DragHandle(
                    color = Gray300,
                    width = 60.dp
                )
                Spacer(Modifier.height(10.dp))
            } else {
                Spacer(Modifier.systemBarsPadding())
            }
            content()
        }
    }
}
