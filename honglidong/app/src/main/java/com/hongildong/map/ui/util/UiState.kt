package com.hongildong.map.ui.util


sealed interface UiState {
    /** 초기 상태 (아무것도 하지 않음) */
    object Initial : UiState

    /** 로그인 진행 중 상태 */
    object Loading : UiState

    /** 로그인 성공 상태 */
    object Success : UiState

    /** 로그인 실패 상태 (실패 메시지를 포함할 수 있음) */
    data class Error(val message: String?) : UiState
}