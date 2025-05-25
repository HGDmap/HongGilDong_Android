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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.hongildong.map.R
import com.hongildong.map.ui.theme.Black
import com.hongildong.map.ui.theme.Gray500
import com.hongildong.map.ui.theme.PrimaryMid
import com.hongildong.map.ui.theme.White
import com.hongildong.map.ui.util.CustomTextField

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onGoSignupClick: () -> Unit,
    onGoBackClick: () -> Unit
) {
    var emailState by remember { mutableStateOf("") }
    var passwordState by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
            .systemBarsPadding(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        Row (
            modifier = Modifier.fillMaxWidth().padding(top = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
        ) {
            Image(
                painter = painterResource(R.drawable.ic_back),
                contentDescription = "go back button",
                modifier = Modifier
                    .padding(start = 10.dp)
                    .clickable { onGoBackClick() }
            )
            Spacer(Modifier.width(20.dp))
            Text(
                "로그인",
                style = MaterialTheme.typography.titleLarge.copy(color = Black),
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CustomTextField(
                placeholderMessage = "이메일을 입력해주세요",
                textState = emailState,
                onTextChange = { emailState = it }
            )
            Spacer(Modifier.height(20.dp))
            CustomTextField(
                placeholderMessage = "비밀번호를 입력해주세요",
                textState = passwordState,
                onTextChange = { passwordState = it },
                isPassword = true
            )
            Spacer(Modifier.height(20.dp))
            Button(
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonColors(
                    containerColor = PrimaryMid,
                    disabledContainerColor = PrimaryMid,
                    contentColor = White,
                    disabledContentColor = White
                ),
                onClick = {
                    /* todo: api 연결시 바꿔야함 */
                    onLoginSuccess()
                }
            ) {
                Text(
                    "로그인",
                    style = MaterialTheme.typography.titleMedium.copy(color = White)
                )
            }
            Spacer(Modifier.height(10.dp))
            Text(
                "회원가입",
                style = MaterialTheme.typography.labelMedium.copy(color = Gray500),
                modifier = Modifier
                    .clickable { onGoSignupClick() }
            )
        }
    }
}

