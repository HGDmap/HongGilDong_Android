package com.hongildong.map.ui.home

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.hongildong.map.R
import com.hongildong.map.ui.profile.MemberViewmodel
import com.hongildong.map.ui.profile.ProfileUpdateContent
import com.hongildong.map.ui.search.location_detail.facility.review.FacilityReviews
import com.hongildong.map.ui.theme.AppTypography
import com.hongildong.map.ui.theme.Black
import com.hongildong.map.ui.theme.Gray500
import com.hongildong.map.ui.theme.Gray700
import com.hongildong.map.ui.theme.PrimaryMid
import com.hongildong.map.ui.theme.White
import com.hongildong.map.ui.util.EmptyContents
import com.hongildong.map.ui.util.ProfileImage
import com.hongildong.map.ui.util.bottomsheet.BottomSheetViewModel

@Composable
fun ProfileScreen(
    bottomSheetViewModel: BottomSheetViewModel = hiltViewModel()
) {
    val memberViewModel: MemberViewmodel = hiltViewModel()

    val pages = listOf(
        ProfileTab(R.drawable.ic_profile_liked_reviews, "좋아요한 리뷰"),
        ProfileTab(R.drawable.ic_profile_my_review, "내 리뷰"),
        ProfileTab(R.drawable.ic_profile_location_suggest, "장소 제안"),
    )
    var tabState by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        memberViewModel.verifyUser()
        memberViewModel.getProfile()
        memberViewModel.getMyReviews()
        memberViewModel.getLikedReview()
    }

    val profileInfo by memberViewModel.profileInfo.collectAsState()
    val myReviews by memberViewModel.myReviews.collectAsState()
    val likedReviews by memberViewModel.likedReviews.collectAsState()
    val isUser by memberViewModel.isUser.collectAsState()

    Column (
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .background(White),
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ProfileImage(
                profileUrl = if (isUser) profileInfo?.profilePic else null
            )
            Text(
                text = if (isUser) profileInfo?.nickname ?: "홍길동" else "로그인 해주세요.",
                style = AppTypography.Bold_18.copy(color = Black),
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 10.dp)
            )
            Image(
                painter = painterResource(R.drawable.ic_edit),
                contentDescription = "프로필 수정하기",
                colorFilter = ColorFilter.tint(Black),
                modifier = Modifier
                    .size(24.dp)
                    .clickable {
                        if (isUser) {
                            bottomSheetViewModel.show {
                                ProfileUpdateContent(
                                    profileUrl = profileInfo?.profilePic,
                                    nickname = profileInfo?.nickname ?: "",
                                    onDone = { imageUri: String?, nickName: String ->
                                        memberViewModel.updateProfile(
                                            pickedImage = imageUri,
                                            nickname = nickName,
                                            userId = profileInfo?.id ?: 0,
                                        )
                                        bottomSheetViewModel.hide()
                                    }
                                )
                            }
                        }
                    }
            )
        }
        TabRow (
            // 1. 현재 선택된 탭의 인덱스
            selectedTabIndex = tabState,
            containerColor = White,
            contentColor = Black,
            // 2. 인디케이터 커스터마이징
            indicator = { },
            divider = {}
        ) {
            pages.forEachIndexed { index, profileTab ->
                Tab(
                    selected = tabState == index,
                    onClick = { tabState = index },
                    text = {
                        Column (
                            modifier = Modifier.padding(5.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(
                                painter = painterResource(profileTab.icon),
                                contentDescription = null,
                                colorFilter = ColorFilter.tint(if (tabState == index) PrimaryMid else Gray700),
                                modifier = Modifier.size(25.dp)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = profileTab.title,
                                style = AppTypography.Bold_13.copy(color = if (tabState == index) PrimaryMid else Gray700)
                            )
                        }
                    },
                    selectedContentColor = PrimaryMid,
                    unselectedContentColor = Gray500,
                )
            }
        }
        HorizontalDivider(Modifier.height(1.dp), color = Gray500)
        when (tabState) {
            0 -> {
                // 좋아요한 리뷰 탭
                if (isUser) {
                    FacilityReviews(
                        reviews = myReviews,
                        onDeleteItem = {  },
                        onEditItem = {  }
                    )
                } else {
                    EmptyContents("로그인 후에 볼 수 있어요.")
                }
            }
            1 -> {
                // 리뷰 탭
                if (isUser) {
                    FacilityReviews(
                        reviews = likedReviews,
                        onDeleteItem = {  },
                        onEditItem = {  }
                    )
                } else {
                    EmptyContents("로그인 후에 볼 수 있어요.")
                }
            }
            2 -> {
                // 장소 제안 탭
            }
            else -> {
                // 좋아요한 리뷰 탭
                if (isUser) {
                    FacilityReviews(
                        reviews = myReviews,
                        onDeleteItem = {  },
                        onEditItem = {  }
                    )
                } else {
                    EmptyContents("로그인 후에 볼 수 있어요.")
                }
            }
        }
    }

}


data class ProfileTab(val icon: Int, val title: String)