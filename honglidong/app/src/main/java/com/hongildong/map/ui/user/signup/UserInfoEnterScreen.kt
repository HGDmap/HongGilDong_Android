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
fun UserInfoEnterScreen(
    onGoBackClick: () -> Unit,
    onSignupClick: () -> Unit,
    signupViewmodel: SignupViewmodel = hiltViewModel()
) {
    val nameState by signupViewmodel.nameInfo.collectAsState()
    val nicknameState by signupViewmodel.nicknameInfo.collectAsState()
    val buildingState by signupViewmodel.buildingInfo.collectAsState()

    val focusManager = LocalFocusManager.current
    val nicknameFocusRequester = remember { FocusRequester() }
    val buildingFocusRequester = remember { FocusRequester() }

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

            Text("회원 정보 입력", style = AppTypography.Bold_22)
            Spacer(Modifier.height(35.dp))

            Text("이름", style = AppTypography.Bold_15)
            CustomUnderLineTextField(
                placeholderMessage = "성을 포함한 본명을 입력해주세요.",
                textState = nameState,
                onTextChange = { signupViewmodel.onNameInfoChange(it) },
                onEditDone = {
                    nicknameFocusRequester.requestFocus()
                },
            )
            Spacer(Modifier.height(31.dp))

            Text("닉네임", style = AppTypography.Bold_15)
            Row {
                CustomUnderLineTextField(
                    modifier = Modifier.weight(1f).focusRequester(nicknameFocusRequester),
                    placeholderMessage = "사용할 닉네임을 입력해주세요.",
                    textState = nicknameState,
                    onTextChange = { signupViewmodel.onNicknameInfoChange(it) },
                    onEditDone = {
                        buildingFocusRequester.requestFocus()
                    },
                )
                SmallButton(
                    buttonText = "중복확인",
                    isButtonEnabled = nicknameState.isNotEmpty(),
                    onClick = {
                        // todo: 닉네임 중복확인 api 연결
                    },
                )
            }
            Spacer(Modifier.height(31.dp))

            Text("전공 강의동 (선택)", style = AppTypography.Bold_15)
            CustomUnderLineTextField(
                modifier = Modifier.focusRequester(buildingFocusRequester),
                placeholderMessage = "전공 강의동을 입력해주세요.",
                textState = buildingState ?: "",
                onTextChange = { signupViewmodel.onBuildingInfoChange(it.ifEmpty { null }) },
                onEditDone = {
                    focusManager.clearFocus()
                    onSignupClick()
                },
            )
        }

        BottomButton(
            buttonText = "회원 가입하기",
            isButtonEnabled = (nameState.isNotEmpty()) and (nicknameState.isNotEmpty()),
            onClick = {
                onSignupClick()
            }
        )
    }
}