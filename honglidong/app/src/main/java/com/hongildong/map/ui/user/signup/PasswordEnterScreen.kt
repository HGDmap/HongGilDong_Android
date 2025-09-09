package com.hongildong.map.ui.user.signup

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.hongildong.map.R
import com.hongildong.map.ui.theme.AppTypography
import com.hongildong.map.ui.theme.Gray500
import com.hongildong.map.ui.theme.TypeEvent
import com.hongildong.map.ui.theme.White
import com.hongildong.map.ui.util.BottomButton
import com.hongildong.map.ui.util.CustomUnderLineTextField
import com.hongildong.map.ui.util.HeaderWithGoBack

@Composable
fun PasswordEnterScreen (
    onGoBackClick: () -> Unit,
    onNextClick: () -> Unit,
    signupViewmodel: SignupViewmodel = hiltViewModel()
) {
    val passwordState by signupViewmodel.passwordInfo.collectAsState()
    var passwordCheckState by remember { mutableStateOf("") }

    val focusManager = LocalFocusManager.current
    val checkFocusRequester = remember { FocusRequester() }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .padding(20.dp)
            .background(White),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        HeaderWithGoBack(
            onGoBackClick = onGoBackClick,
            title = stringResource(R.string.signup)
        )

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Spacer(Modifier.height(80.dp))

            Text("비밀번호 입력", style = AppTypography.Bold_22)
            CustomUnderLineTextField(
                placeholderMessage = "사용할 비밀번호를 입력해주세요.",
                textState = passwordState,
                onTextChange = { signupViewmodel.onPasswordInfoChange(it) },
                onEditDone = {
                    checkFocusRequester.requestFocus()
                },
                isPassword = true
            )
            Spacer(Modifier.height(31.dp))

            Text("비밀번호 확인", style = AppTypography.Bold_22)
            CustomUnderLineTextField(
                modifier = Modifier.focusRequester(checkFocusRequester),
                placeholderMessage = "비밀번호를 다시 한번 입력해주세요.",
                textState = passwordCheckState,
                onTextChange = { passwordCheckState = it },
                onEditDone = {
                    focusManager.clearFocus()
                },
                isPassword = true
            )
            if (passwordCheckState.isNotEmpty() and (passwordCheckState != passwordState)) {
                Text(
                    "입력한 비밀번호가 달라요.",
                    style = AppTypography.Medium_11.copy(color = TypeEvent)
                )
            }
        }

        Column (
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "다음이 마지막 단계예요!",
                style = AppTypography.Medium_15.copy(Gray500)
            )
            Spacer(Modifier.height(10.dp))
            BottomButton(
                buttonText = "계속하기",
                isButtonEnabled = passwordCheckState == passwordState,
                onClick = {
                    onNextClick()
                }
            )
        }
    }
}