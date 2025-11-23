package com.hongildong.map.ui.util

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import com.hongildong.map.ui.theme.Gray200
import com.hongildong.map.ui.theme.White
import kotlinx.coroutines.launch
import kotlin.math.abs

// Wheel Picker
@Composable
fun WheelPicker(
    modifier: Modifier = Modifier,
    items: List<String>,
    initialItem: String,
    onItemSelected: (Int, String) -> Unit = { _, _ -> },
    content: @Composable ((String, Boolean) -> Unit)
) {
    val density = LocalDensity.current
    val scrollState = rememberLazyListState(0)
    var lastSelectedIndex by remember { mutableStateOf(0) }
    val coroutineScope = rememberCoroutineScope()

    val itemHeight = 45.dp
    val itemHeightPx = with(density) { itemHeight.toPx() }

    Column(modifier) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .drawWithContent {
                    drawContent()
                    val centerY = size.height / 2f
                    val rectTop = centerY - (itemHeightPx / 2f)
                    val rectHeight = itemHeightPx
                    drawRoundRect(
                        color = Gray200,
                        cornerRadius = CornerRadius(8.dp.toPx()),
                        blendMode = BlendMode.Multiply,
                        topLeft = Offset(0f, rectTop),
                        size = Size(size.width, rectHeight)
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            val availableHeight = this.constraints.maxHeight.toFloat()
            val currentPickerHeightPx = if (availableHeight == Constraints.Infinity.toFloat()) {
                with(density) { 220.dp.toPx() }
            } else {
                availableHeight
            }

            LaunchedEffect(currentPickerHeightPx) {
                val targetIndex = items.indexOf(initialItem)
                val safeTargetIndex = if (targetIndex >= 0) targetIndex else 0

                lastSelectedIndex = safeTargetIndex
                scrollState.scrollToItem(safeTargetIndex)
            }

            val pickerHeightDp = with(density) { currentPickerHeightPx.toDp() }
            val fadeHeightDp =
                with(density) { ((currentPickerHeightPx - itemHeightPx) / 2f).toDp() }

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(pickerHeightDp),
                state = scrollState,
                flingBehavior = rememberSnapFlingBehavior(scrollState),
                contentPadding = PaddingValues(vertical = fadeHeightDp)
            ) {
                items(
                    count = items.size,
                    itemContent = { i ->
                        val item = items[i]

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(itemHeight)
                                .pointerInput(i) {
                                    detectTapGestures(
                                        onTap = {
                                            coroutineScope.launch {
                                                scrollState.animateScrollToItem(i)
                                            }
                                        }
                                    )
                                }
                                .onGloballyPositioned { coordinates ->
                                    val y = (coordinates.positionInParent().y) + (itemHeightPx / 2f)
                                    val parentHalfHeight = (currentPickerHeightPx / 2f)
                                    val isCurrentlySelected = abs(parentHalfHeight - y) <= (itemHeightPx / 2f)

                                    if (isCurrentlySelected && lastSelectedIndex != i && item.isNotEmpty()) {
                                        onItemSelected(i, item)
                                        lastSelectedIndex = i
                                    }
                                },
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            content(item, lastSelectedIndex == i)
                        }
                    }
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(fadeHeightDp)
                    .align(Alignment.TopCenter)
                    .drawWithContent {
                        drawRect(
                            brush = Brush.verticalGradient(
                                colors = listOf(White, White.copy(alpha = 0f))
                            )
                        )
                    }
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(fadeHeightDp)
                    .align(Alignment.BottomCenter)
                    .drawWithContent {
                        drawRect(
                            brush = Brush.verticalGradient(
                                colors = listOf(White.copy(alpha = 0f), White)
                            )
                        )
                    }
            )
        }
    }
}