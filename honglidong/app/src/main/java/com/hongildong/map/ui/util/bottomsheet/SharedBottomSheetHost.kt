package com.hongildong.map.ui.util.bottomsheet


import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.hongildong.map.ui.theme.Gray300
import kotlinx.coroutines.flow.collectLatest

/**
 * BottomSheetViewModel의 상태를 구독하여,
 * ModalBottomSheet를 실제로 화면에 그리거나 숨기는 역할을 하는 '호스트' Composable입니다.
 *
 * @param viewModel 앱 전역의 바텀시트 ViewModel.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SharedBottomSheetHost(
    viewModel: BottomSheetViewModel = hiltViewModel<BottomSheetViewModel>()
) {
    val content by viewModel.sheetContent.collectAsState()
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true // 중간에 걸치는 상태 없이 완전히 펼쳐지도록 설정
    )

    // ViewModel의 content 상태가 변경될 때를 감지합니다.
    LaunchedEffect(content) {
        if (content != null) {
            sheetState.show()
        } else {
            sheetState.hide()
        }
    }

    // ModalBottomSheet를 렌더링할지 여부를 결정합니다.
    // content가 null이 아닐 때만 ModalBottomSheet를 그립니다.
    if (content != null) {
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = { viewModel.hide() }, // 바깥 영역 클릭 시 숨김 처리
            // 원하는 공통 스타일을 여기에 적용할 수 있습니다.
            // containerColor = White,
            dragHandle = {
                BottomSheetDefaults.DragHandle(
                    color = Gray300,
                    width = 60.dp
                )
            },
        ) {
            // ViewModel로부터 전달받은 Composable 콘텐츠를 여기에 렌더링합니다.
            content!!()
        }
    }
}