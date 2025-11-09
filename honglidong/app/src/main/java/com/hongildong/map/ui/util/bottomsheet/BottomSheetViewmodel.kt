package com.hongildong.map.ui.util.bottomsheet

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject


@HiltViewModel
class BottomSheetViewModel @Inject constructor() : ViewModel() {

    // 바텀 시트에 표시될 Composable 콘텐츠를 저장하는 StateFlow.
    // null이면 바텀 시트가 숨겨진 상태입니다.
    private val _sheetContent = MutableStateFlow<(@Composable ColumnScope.() -> Unit)?>(null)
    val sheetContent = _sheetContent.asStateFlow()

    /**
     * 바텀 시트를 표시하고, 내부에 표시할 콘텐츠를 설정합니다.
     * @param content 바텀 시트 내부에 표시될 Composable 람다.
     */
    fun show(content: @Composable ColumnScope.() -> Unit) {
        _sheetContent.value = content
    }

    /**
     * 바텀 시트를 숨깁니다.
     */
    fun hide() {
        _sheetContent.value = null
    }
}