package com.hongildong.map.ui.search.location_detail.facility.review

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.hongildong.map.R
import com.hongildong.map.data.entity.Floor
import com.hongildong.map.ui.search.location_detail.facility.review.ReviewViewModel
import com.hongildong.map.ui.theme.AppTypography
import com.hongildong.map.ui.theme.Black
import com.hongildong.map.ui.theme.Gray300
import com.hongildong.map.ui.theme.Gray500
import com.hongildong.map.ui.theme.Gray600
import com.hongildong.map.ui.theme.PrimaryLight
import com.hongildong.map.ui.theme.PrimaryMid
import com.hongildong.map.ui.theme.White
import com.hongildong.map.ui.util.BottomButton
import com.hongildong.map.ui.util.CustomLoading
import com.hongildong.map.ui.util.CustomTextBox
import com.hongildong.map.ui.util.NetworkImage
import com.hongildong.map.ui.util.UiState
import com.hongildong.map.ui.util.WheelPicker
import com.hongildong.map.ui.util.popup.ConfirmPopup

@Composable
fun ReviewScreen(
    reviewViewModel: ReviewViewModel = hiltViewModel(),
    facilityName: String,
    reviewMode: Int,
    onGoBack: () -> Unit,
    onDone: (String, String, String) -> Unit // content, 이런점이좋아요, rate 순서
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    // 이런점이 좋아요
    var selectedItem by remember { mutableStateOf("") }

    // rate
    var rateValue by remember { mutableStateOf("5.0") }

    // 기존 리뷰 정보 - reviewmode == 1(수정)일때 사용
    val targetReview by reviewViewModel.targetReview.collectAsState()

    var textState by remember { mutableStateOf("") }

    // 취소 2버튼 팝업 상태
    var showDialog by remember { mutableStateOf(false) }

    // 프로그래스바 상태
    var showProgress by remember { mutableStateOf(false) }
    val loadingState by reviewViewModel.uploadState.collectAsState()

    LaunchedEffect(loadingState) {
        when (loadingState) {
            is UiState.Initial -> showProgress = false
            is UiState.Error -> {
                showProgress = false
                Toast.makeText(context, "리뷰 작성에 실패했습니다.", Toast.LENGTH_SHORT).show()
            }
            is UiState.Loading -> showProgress = true
            is UiState.Success -> {
                showProgress = false
                onGoBack()
            }
        }
    }

    LaunchedEffect(targetReview, textState) {
        if (reviewMode == 1 && targetReview != null) {
            // 리뷰 수정 상황이라면 기존 리뷰 컨텐츠 덮어쓰기
            textState = targetReview!!.content
            rateValue = targetReview!!.rating.toString()
        }
    }

    // 고른 이미지 리스트
    val images by reviewViewModel.selectedImageUris.collectAsState()

    // 이미지 고르기 런처
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(),
        onResult = { uris ->
            reviewViewModel.addImage(uris)
        }
    )

    Box(
        modifier = Modifier
            .background(White)
            .fillMaxSize()
            .systemBarsPadding(),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = 10.dp)
        ) {
            // 화면 상단 시설 정보와 뒤로가기 버튼
            Box(
                modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp, horizontal = 20.dp)
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
                        .size(20.dp)
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
                    .weight(1f)
                    .verticalScroll(scrollState),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(25.dp))

                // rate picker
                WheelPicker(
                    modifier = Modifier
                        .height(150.dp)
                        .fillMaxWidth()
                        //.width(220.dp)
                        .padding(horizontal = 10.dp),
                    items = listOf("5.0", "4.5", "4.0", "3.5", "3.0", "2.5", "2.0", "1.5", "1.0", "0.5"),
                    initialItem = rateValue,
                    onItemSelected = { index, selectedValue ->
                        rateValue = selectedValue
                    }
                )
                { item, isSelected ->
                    if (item.toString().isNotEmpty()) {
                        Row(
                            modifier = Modifier.padding(vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RateImage(
                                width = 181.dp,
                                height = 31.dp,
                                rate = item.toFloat(),
                                color = if (isSelected) PrimaryMid else Gray500
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(
                                text = item.toString(),
                                style = AppTypography.Medium_15.copy(color = if (isSelected) Black else Gray500),
                            )
                        }
                    }
                }
                Spacer(Modifier.height(25.dp))

                HorizontalDivider(thickness = 1.dp, color = Gray300)

                // 어떤점이 좋았나요
                RecommendSelection(
                    selectedItemApiName = selectedItem,
                    onItemSelected = { selectedItem = it }
                )

                Spacer(Modifier.height(20.dp))
                Text(
                    "후기를 작성해보세요!",
                    style = AppTypography.Bold_20.copy(color = Black),
                    textAlign = TextAlign.Start,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 25.dp, horizontal = 10.dp)
                )
                Spacer(Modifier.height(12.dp))
                // 사진 리스트 보여주기
                Row(
                    modifier = Modifier.fillMaxWidth().padding(start = 10.dp),
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
                    if (images.isNotEmpty()) {
                        LazyRow (
                            modifier = Modifier.weight(1f),
                            contentPadding = PaddingValues(horizontal = 5.dp),
                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            items(images) { imageUri ->
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

                Spacer(Modifier.height(20.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp)
                ) {
                    CustomTextBox(
                        placeholderMessage = "장소가 마음에 들었나요?\n자세한 후기를 적어주세요!",
                        textState = textState,
                        onTextChange = { textState = it }
                    )
                }
                Spacer(Modifier.height(20.dp))
            }

            // bottom button
            BottomButton(
                buttonText = "작성 완료",
                isButtonEnabled = if ((textState.length > 10) and (selectedItem.isNotEmpty())) true else false,
                onClick = {
                    onDone(textState, selectedItem, rateValue)
                }
            )
        }

        if (showDialog) {
            ConfirmPopup(
                message = "리뷰 작성을 취소하시겠습니까?",
                dismissMsg = "취소",
                confirmMsg = "확인",
                onDismissRequest = {showDialog = false},
                onConfirmation = {
                    showDialog = false
                    onGoBack()
                }
            )
        }

        if (showProgress) {
            CustomLoading()
        }
    }
}

@Composable
fun RecommendSelection(
    selectedItemApiName: String,
    onItemSelected: (String) -> Unit
) {
    FacilityRecommendType.entries.forEach {
        RecommendTypeItem(
            type = it,
            isSelected = selectedItemApiName == it.apiName,
            onClick = {
                onItemSelected(it.apiName)
            }
        )
    }
}

@Composable
fun RecommendTypeItem(
    type: FacilityRecommendType,
    selectedCnt: Int? = null,
    isSelected: Boolean,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .padding(3.dp)
            .fillMaxWidth()
            .border(width = 1.dp, color = if (isSelected) PrimaryMid else Gray300, shape = RoundedCornerShape(12.dp))
            .background(color = if (isSelected) PrimaryLight.copy(alpha = 0.4f) else White, shape = RoundedCornerShape(12.dp))
            .padding(10.dp)
            .clickable {
                onClick()
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Image(
            painter = painterResource(type.icon),
            contentDescription = null,
            modifier = Modifier.padding(horizontal = 5.dp)
        )
        Text(
            text = type.displayName,
            style = AppTypography.Medium_15.copy(color = if (isSelected) PrimaryMid else Gray600),
            modifier = Modifier.weight(1f)
        )
        if (selectedCnt != null) {
            Text(
                text = selectedCnt.toString(),
                style = AppTypography.Medium_15.copy(color = if (isSelected) PrimaryMid else Gray600),
                modifier = Modifier.weight(1f)
            )
        }
    }
}

enum class FacilityRecommendType(val apiName: String, val displayName: String, val icon: Int) {
    STUDY("STUDY", "공부하기 좋아요", R.drawable.ic_good_to_study),
    REST("REST", "쉬기 좋아요", R.drawable.ic_good_to_rest),
    VIEW("VIEW", "경치가 좋아요", R.drawable.ic_good_to_view),
    MEETING("MEETING", "회의하기 좋아요", R.drawable.ic_good_to_meeting),
    FOOD("FOOD", "맛있어요", R.drawable.ic_good_to_food);

    companion object {
        // apiName 기반으로 enum 상수 검색
        fun fromApiName(apiName: String): FacilityRecommendType? {
            return FacilityRecommendType.entries.find { it.apiName.equals(apiName, ignoreCase = true) }
        }
        // displayName 기반으로 enum 상수 검색
        fun fromDisplayName(displayName: String): FacilityRecommendType? {
            return FacilityRecommendType.entries.find { it.displayName.equals(displayName, ignoreCase = true) }
        }
    }
}