package com.hongildong.map.ui.user

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.hongildong.map.R
import com.hongildong.map.ui.theme.AppTypography
import com.hongildong.map.ui.theme.Gray500
import com.hongildong.map.ui.theme.White
import com.hongildong.map.ui.util.BottomButton
import com.hongildong.map.ui.util.CustomUnderLineTextField
import com.hongildong.map.ui.util.HeaderWithGoBack

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onGoSignupClick: () -> Unit,
    onGoBackClick: () -> Unit
) {
    var emailState by remember { mutableStateOf("") }
    var passwordState by remember { mutableStateOf("") }

    val focusManager = LocalFocusManager.current
    val passwordFocusRequester = remember { FocusRequester() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
            .systemBarsPadding()
            .padding(20.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        HeaderWithGoBack(
            onGoBackClick = onGoBackClick,
            stringResource(R.string.login)
        )

        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(Modifier.height(50.dp))
            Text(
                text = "홍대생 인증을 해주세요!",
                style = AppTypography.Bold_22
            )

            Column (
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {

                CustomUnderLineTextField(
                    placeholderMessage = stringResource(R.string.email_placeholder),
                    textState = emailState,
                    onTextChange = { emailState = it },
                    onEditDone = {
                        passwordFocusRequester.requestFocus()
                    },
                    suffix = "@g.hongik.ac.kr"
                )
                Spacer(Modifier.height(20.dp))
                CustomUnderLineTextField(
                    modifier = Modifier.focusRequester(passwordFocusRequester),
                    placeholderMessage = stringResource(R.string.password_placeholder),
                    textState = passwordState,
                    onTextChange = { passwordState = it },
                    isPassword = true,
                    onEditDone = {
                        focusManager.clearFocus()
                        // todo: 여기에 로그인 로직 추가 - 키보드 완료 버튼으로도 로그인 가능하게
                    }
                )
                // todo: api 연결시 로그인 오류 메시지 추가
                /*Spacer(Modifier.height(8.dp))
                Text(
                    text = "올바르지 않은 회원 정보입니다.",
                    style = AppTypography.Medium_11.copy(TypeEvent)
                )*/
            }

            Column (
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                BottomButton(
                    buttonText = stringResource(R.string.login),
                    isButtonEnabled = (emailState.isNotEmpty()) and (passwordState.isNotEmpty()),
                    onClick = onLoginSuccess
                )
                Spacer(Modifier.height(10.dp))
                Text(
                    stringResource(R.string.signup),
                    style = AppTypography.Medium_15.copy(color = Gray500),
                    modifier = Modifier
                        .clickable { onGoSignupClick() }
                )
            }
        }
    }
}



