package com.hongildong.map.ui.user.signup

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class TermsViewmodel: ViewModel() {
    // 약관 데이터
    private val termsData = listOf(
        Term(
            id = 0,
            isRequired = true,
            title = "[필수] 서비스 이용 약관 및 개인정보 수집·이용 동의",
            content = "1. 수집하는 개인정보 항목\n" +
                    "\n" +
                    "실명, 이메일, 닉네임, 비밀번호\n" +
                    "\n" +
                    "2. 개인정보 수집·이용 목적\n" +
                    "\n" +
                    "실명: 본인 확인 및 사용자 식별\n" +
                    "\n" +
                    "이메일: 서비스 관련 고지사항 전달, 계정 인증\n" +
                    "\n" +
                    "닉네임: 서비스 내 표시 이름(다른 사용자와의 구분)\n" +
                    "\n" +
                    "비밀번호: 계정 보안 및 로그인 기능 제공\n" +
                    "\n" +
                    "3. 보유 및 이용 기간\n" +
                    "\n" +
                    "회원 탈퇴 후 1개월 간 보관 후 파기\n" +
                    "\n" +
                    "단, 관련 법령에서 정한 경우 해당 기간 동안 보관할 수 있습니다.\n" +
                    "\n" +
                    "위 사항에 동의하지 않으면 서비스 가입 및 이용이 불가합니다."
        ),
        Term(
            id = 1,
            isRequired = false,
            title = "[선택] 전공 강의동 정보 수집 및 이용 동의",
            content = "1. 수집·이용 항목\n" +
                    "\n" +
                    "전공 강의동\n" +
                    "\n" +
                    "2. 이용 목적\n" +
                    "\n" +
                    "개인화된 길찾기 및 위치 안내 서비스 제공\n" +
                    "\n" +
                    "3. 보유 및 이용 기간\n" +
                    "\n" +
                    "회원 탈퇴 후 1개월까지 또는 동의 철회 시까지\n" +
                    "\n" +
                    "위 사항에 동의하지 않더라도 서비스 이용에는 제한이 없습니다."
        )
    )

    // 원본 약관 리스트
    private val _terms = MutableStateFlow<List<Term>>(emptyList())
    val terms: StateFlow<List<Term>> = _terms.asStateFlow()

    // 각 약관의 체크 상태를 관리
    private val _checkedState = MutableStateFlow<Map<Int, Boolean>>(emptyMap())
    val checkedState: StateFlow<Map<Int, Boolean>> = _checkedState.asStateFlow()

    // 약관 뷰모델 초기 데이터 설정
    init {
        _terms.value = termsData // Repository 대신 내부 데이터 사용
        _checkedState.value = termsData.associate { it.id to false }
    }

    // 약관의 체크 상태 변경
    fun onTermCheckedChange(termId: Int, isChecked: Boolean) {
        _checkedState.value = _checkedState.value.toMutableMap().apply {
            this[termId] = isChecked
        }
    }
}