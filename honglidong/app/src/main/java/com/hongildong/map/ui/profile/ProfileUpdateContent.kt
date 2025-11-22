package com.hongildong.map.ui.profile

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.hongildong.map.R
import com.hongildong.map.ui.theme.AppTypography
import com.hongildong.map.ui.theme.Black
import com.hongildong.map.ui.theme.Gray500
import com.hongildong.map.ui.util.BottomButton
import com.hongildong.map.ui.util.CustomTextField
import com.hongildong.map.ui.util.ProfileImage

@Composable
fun ProfileUpdateContent(
    profileUrl: String?,
    nickname: String,
    onDone: (String?, String) -> Unit
) {
    var profileImage by remember { mutableStateOf(profileUrl) }
    var textState by remember { mutableStateOf(nickname) }

    // 이미지 고르기 런처
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            profileImage = uri.toString()
        }
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("프로필 수정", style = AppTypography.Bold_20)
        Spacer(Modifier.height(20.dp))
        Box {
            ProfileImage(
                profileUrl = profileUrl,
                modifier = Modifier.size(88.dp)
            )
            Image(
                painter = painterResource(R.drawable.ic_change_profile_image),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .shadow(1.dp, shape = CircleShape)
                    .clickable {
                        photoPickerLauncher.launch(
                            input = PickVisualMediaRequest(
                                ActivityResultContracts.PickVisualMedia.ImageOnly
                            )
                        )
                    }
            )
        }

        Spacer(Modifier.height(20.dp))
        Text(
            "닉네임",
            style = AppTypography.Medium_15.copy(color = Gray500),
            textAlign = TextAlign.Start,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(4.dp))
        CustomTextField(
            placeholderMessage = "닉네임을 입력해주세요",
            textState = textState,
            onTextChange = { textState = it },
            onSearch = {},
            textStyle = AppTypography.Bold_20.copy(color = Black)
        )
        Spacer(Modifier.height(30.dp))

        BottomButton(
            buttonText = "완료",
            isButtonEnabled = textState.isNotEmpty(),
            onClick = {
                onDone(profileImage, textState)
                profileImage = ""
                textState = ""
            }
        )
    }
}