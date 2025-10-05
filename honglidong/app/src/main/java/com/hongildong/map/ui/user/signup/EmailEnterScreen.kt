package com.hongildong.map.ui.user.signup

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import com.hongildong.map.ui.theme.White
import com.hongildong.map.ui.util.BottomButton
import com.hongildong.map.ui.util.CustomUnderLineTextField
import com.hongildong.map.ui.util.HeaderWithGoBack
import com.hongildong.map.ui.util.SmallButton

@Composable
fun EmailEnterScreen (
    onGoBackClick: () -> Unit,
    onNextClick: () -> Unit,
    authViewmodel: AuthViewmodel
) {
    val emailState by authViewmodel.emailInfo.collectAsState()
    var numberState by remember { mutableStateOf("") }

    val focusManager = LocalFocusManager.current
    val numberFocusRequester = remember { FocusRequester() }

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

            Text("이메일 입력", style = AppTypography.Bold_22)
            Row {
                CustomUnderLineTextField(
                    modifier = Modifier.weight(1f),
                    placeholderMessage = "이메일",
                    textState = emailState,
                    onTextChange = { authViewmodel.onEmailInfoChange(it) },
                    onEditDone = {
                        numberFocusRequester.requestFocus()
                    },
                    suffix = "@g.hongik.ac.kr"
                )
                SmallButton(
                    buttonText = "인증번호 전송",
                    isButtonEnabled = emailState.isNotEmpty(),
                    onClick = {
                        // todo: 닉네임 중복확인 api 연결
                    },
                )
            }
            Spacer(Modifier.height(31.dp))

            Text("인증번호 입력", style = AppTypography.Bold_22)
            Row {
                CustomUnderLineTextField(
                    modifier = Modifier
                        .weight(1f)
                        .focusRequester(numberFocusRequester),
                    placeholderMessage = "인증번호를 입력해주세요.",
                    textState = numberState,
                    onTextChange = { numberState = it },
                    onEditDone = {
                        focusManager.clearFocus()
                        // todo: 인증번호 확인 api 연결
                    },
                )
                SmallButton(
                    buttonText = "인증번호 확인",
                    isButtonEnabled = numberState.isNotEmpty(),
                    onClick = {
                        // todo: 인증번호 확인 api 연결
                    },
                )
            }
        }

        BottomButton(
            buttonText = "계속하기",
            isButtonEnabled = (emailState.isNotEmpty()) and (numberState.isNotEmpty()), // todo: 추후 인증번호 옳을때 계속하기 되는걸로 바꾸기
            onClick = {
                onNextClick()
            }
        )
    }
}