package com.hongildong.map.ui.user.signup

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TriStateCheckbox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hongildong.map.R
import com.hongildong.map.ui.theme.AppTypography
import com.hongildong.map.ui.theme.Gray400
import com.hongildong.map.ui.theme.PrimaryMid
import com.hongildong.map.ui.theme.White
import com.hongildong.map.ui.util.HeaderWithGoBack

@Composable
fun TermsAgreementScreen(
    onSignupClick: () -> Unit,
    onGoBackClick: () -> Unit,
    viewmodel: TermsViewmodel = viewModel()
) {
    val childCheckedStates = remember { mutableStateListOf(false, false) }
    val parentState = when {
        childCheckedStates.all { it } -> ToggleableState.On
        childCheckedStates.none { it } -> ToggleableState.Off
        else -> ToggleableState.Off
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
            .systemBarsPadding()
            .padding(20.dp),
    ) {
        HeaderWithGoBack(
            onGoBackClick = onGoBackClick,
            stringResource(R.string.signup)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(White),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(Modifier.height(50.dp))
            Text("약관 동의가 필요해요.", style = AppTypography.Bold_24)

            Column (
                Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                // parent Checkbox
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    TriStateCheckbox(
                        state = parentState,
                        onClick = {
                            // Determine new state based on current state
                            val newState = parentState != ToggleableState.On
                            childCheckedStates.forEachIndexed { index, _ ->
                                childCheckedStates[index] = newState
                            }
                        },
                        colors = CheckboxColors(
                            checkedCheckmarkColor = White,
                            uncheckedCheckmarkColor = White,
                            checkedBoxColor = PrimaryMid,
                            uncheckedBoxColor = White,
                            disabledCheckedBoxColor = Gray400,
                            disabledUncheckedBoxColor = Gray400,
                            disabledIndeterminateBoxColor = Gray400,
                            checkedBorderColor = PrimaryMid,
                            uncheckedBorderColor = Gray400,
                            disabledBorderColor = Gray400,
                            disabledUncheckedBorderColor = Gray400,
                            disabledIndeterminateBorderColor = Gray400
                        )
                    )
                    Text(
                        text = "전체 동의",
                        style = AppTypography.Bold_18
                    )
                }
                Spacer(Modifier.height(20.dp))
                HorizontalDivider(thickness = 1.dp, color = Gray400)
                Spacer(Modifier.height(10.dp))

                // Child Checkboxes
                childCheckedStates.forEachIndexed { index, checked ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Checkbox(
                            checked = checked,
                            onCheckedChange = { isChecked ->
                                // Update the individual child state
                                childCheckedStates[index] = isChecked
                            },
                            colors = CheckboxColors(
                                checkedCheckmarkColor = White,
                                uncheckedCheckmarkColor = White,
                                checkedBoxColor = PrimaryMid,
                                uncheckedBoxColor = White,
                                disabledCheckedBoxColor = Gray400,
                                disabledUncheckedBoxColor = Gray400,
                                disabledIndeterminateBoxColor = Gray400,
                                checkedBorderColor = PrimaryMid,
                                uncheckedBorderColor = Gray400,
                                disabledBorderColor = Gray400,
                                disabledUncheckedBorderColor = Gray400,
                                disabledIndeterminateBorderColor = Gray400
                            )
                        )
                        Row (
                            modifier = Modifier
                                .clickable {
                                    // todo: 약관 확인하기 연결
                                },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = terms[index].title,
                                style = AppTypography.Medium_15,
                                modifier = Modifier.weight(1f)
                            )
                            Image(
                                painter = painterResource(R.drawable.ic_next),
                                contentDescription = terms[index].title + "약관 확인하기"
                            )
                        }
                    }
                }
            }

            Button(
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonColors(
                    containerColor = PrimaryMid,
                    disabledContainerColor = PrimaryMid,
                    contentColor = White,
                    disabledContentColor = White
                ),
                onClick = {
                    // todo: 회원 정보 입력 화면 연결
                }
            ) {
                Text(
                    text = stringResource(R.string.continue_button),
                    style = AppTypography.Bold_18.copy(color = White)
                )
            }
        }
    }
}


data class Term(
    val id: Int,
    val isRequired: Boolean,
    val title: String,
    val content: String
)

val terms = listOf(
    Term(
        id = 1,
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
        id = 2,
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