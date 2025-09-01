package com.hongildong.map.ui.user

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TriStateCheckbox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.unit.dp
import com.hongildong.map.R
import com.hongildong.map.ui.theme.AppTypography
import com.hongildong.map.ui.theme.Black
import com.hongildong.map.ui.theme.Gray400
import com.hongildong.map.ui.theme.PrimaryMid
import com.hongildong.map.ui.theme.White

@Composable
fun SignupScreen(
    onSignupClick: () -> Unit,
    onGoBackClick: () -> Unit

) {
    val childCheckedStates = remember { mutableStateListOf(false, false, false, false) }
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
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
        ) {
            Image(
                painter = painterResource(R.drawable.ic_back),
                contentDescription = stringResource(R.string.go_back),
                modifier = Modifier
                    .padding(start = 10.dp)
                    .clickable { onGoBackClick() }
            )
            Spacer(Modifier.width(20.dp))
            Text(
                stringResource(R.string.signup),
                style = AppTypography.Bold_22,
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(White),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            Column (
                Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text("약관 동의가 필요해요.", style = AppTypography.Bold_24)
                Spacer(Modifier.height(30.dp))

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
                                }
                        ) {
                            Text(
                                text = "Option ${index + 1}",
                                style = AppTypography.Medium_15,
                                modifier = Modifier.weight(1f)
                            )
                            Image(
                                painter = painterResource(R.drawable.ic_next),
                                contentDescription = "약관 확인하기"
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