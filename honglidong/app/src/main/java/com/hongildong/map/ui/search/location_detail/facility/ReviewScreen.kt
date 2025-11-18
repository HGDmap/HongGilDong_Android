package com.hongildong.map.ui.search.location_detail.facility

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.hongildong.map.R
import com.hongildong.map.ui.theme.AppTypography
import com.hongildong.map.ui.theme.Black
import com.hongildong.map.ui.theme.Gray300
import com.hongildong.map.ui.theme.PrimaryMid
import com.hongildong.map.ui.util.BottomButton
import com.hongildong.map.ui.util.CustomTextBox
import com.hongildong.map.ui.util.NetworkImage
import com.hongildong.map.ui.util.popup.ConfirmPopup

@Composable
fun ReviewScreen(
    facilityId: Int,
    facilityName: String,
    onGoBack: () -> Unit,
    onDone: () -> Unit
) {
    val scrollState = rememberScrollState()
    var textState by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }

    // 고른 이미지 리스트
    var selectedImageUris by remember { mutableStateOf<List<Uri>>(emptyList()) }

    // 이미지 고르기 런처
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(),
        onResult = { uris ->
            selectedImageUris = uris
        }
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(),
        contentAlignment = Alignment.TopCenter
    ) {
        Column() {
            // 화면 상단 시설 정보와 뒤로가기 버튼
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    facilityName,
                    style = AppTypography.Bold_20.copy(color = Black),
                    modifier = Modifier.align(Alignment.TopCenter)
                )
                Image(
                    painter = painterResource(R.drawable.ic_close),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(color = Black),
                    modifier = Modifier
                        .size(15.dp)
                        .align(Alignment.TopEnd)
                        .clickable {
                            showDialog = true
                        }
                )
                Spacer(Modifier.height(5.dp))
            }

            // 스크롤 가능한 부분: 별점 선택, 어떤점이 좋았나요, 후기 작성(이미지+텍스트)
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
            ) {
                Spacer(Modifier.height(20.dp))
                RateImage(
                    width = 181.dp,
                    height = 31.dp,
                    rate = 0.9f
                )

                HorizontalDivider(thickness = 1.dp, color = Gray300)

                Text(
                    "후기를 작성해보세요!",
                    style = AppTypography.Bold_20.copy(color = Black),
                    modifier = Modifier.padding(vertical = 25.dp)
                )
                // 사진 리스트 보여주기
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 이미지 선택 아이콘
                    Box(
                        modifier = Modifier
                            .size(88.dp)
                            .border(
                                width = 1.dp,
                                color = PrimaryMid,
                                shape = RoundedCornerShape(10.dp)
                            )
                            .clickable {
                                photoPickerLauncher.launch(
                                    // 이미지만 선택할 수 있는 런처를 오픈함
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            }
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Image(
                                painter = painterResource(R.drawable.ic_camera),
                                contentDescription = null,
                                colorFilter = ColorFilter.tint(color = PrimaryMid),
                                modifier = Modifier
                                    .size(35.dp)
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(
                                "사진 등록",
                                style = AppTypography.Medium_13.copy(color = PrimaryMid),
                            )
                        }

                    }
                    // 고른 이미지
                    if (selectedImageUris.isNotEmpty()) {
                        LazyRow (
                            modifier = Modifier.weight(1f)
                        ) {
                            items(selectedImageUris) { imageUri ->
                                NetworkImage(
                                    url = imageUri.toString(),
                                    width = 89.dp,
                                    height = 89.dp,
                                    contentDescription = null,
                                )
                            }
                        }
                    }
                }

                Spacer(Modifier.height(4.dp))
                CustomTextBox(
                    placeholderMessage = "장소가 마음에 들었나요?\n자세한 후기를 적어주세요!",
                    textState = textState,
                    onDone = { onDone() },
                    onTextChange = { textState = it }
                )
            }

            // bottom button
            BottomButton(
                buttonText = "작성완료",
                isButtonEnabled = textState.isNotEmpty(),
                onClick = {
                    onDone()
                }
            )
        }

        if (showDialog) {
            ConfirmPopup(
                message = "리뷰 작성을 취소하시겠습니까?",
                dismissMsg = "취소",
                confirmMsg = "확인",
                onDismissRequest = {showDialog = false},
                onConfirmation = { onGoBack() }
            )
        }
    }
}