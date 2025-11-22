package com.hongildong.map.ui.search.location_detail.facility

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.hongildong.map.R
import com.hongildong.map.ui.search.SearchKeywordViewmodel
import com.hongildong.map.ui.theme.AppTypography
import com.hongildong.map.ui.theme.Black
import com.hongildong.map.ui.theme.White
import com.hongildong.map.ui.util.DummyImage
import com.hongildong.map.ui.util.EmptyContents
import com.hongildong.map.ui.util.NetworkImage
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.haze
import dev.chrisbanes.haze.hazeChild
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials

@OptIn(ExperimentalHazeMaterialsApi::class)
@Composable
fun FacilityPhotoTab(
    searchViewmodel: SearchKeywordViewmodel = hiltViewModel(),
    isUser: Boolean,
    onUpdate: () -> Unit,
) {
    val photos by searchViewmodel.facilityPhotoInfo.collectAsState()
    var imageDetailInfo by remember { mutableStateOf("") }
    val hazeState = remember { HazeState() }

    LaunchedEffect(Unit) {
        if (isUser) {
            onUpdate()
        }
    }

    Box(
        Modifier
            .fillMaxSize()
            .background(White)
    ) {
        if (isUser) {
            // 로그인 되어있을 경우 - 사진 리스트 보여주거나 사진이 없다 보여주기
            if (photos?.imageList?.all { it.isNullOrEmpty() } != false) {
                EmptyContents("등록된 사진이 아직 없어요.")
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 150.dp)
                ) {
                    items(photos?.imageList ?: emptyList()) { photo ->
                        if (!photo.isNullOrEmpty()) {
                            NetworkImage(
                                url = photo,
                                contentDescription = null,
                                modifier = Modifier
                                    .clickable { imageDetailInfo = photo }
                            )
                        }
                    }
                }
            }
        } else {
            // 로그인 안되어 있을 경우 - 유저가 아닐때의 화면 보여주기
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 150.dp),
                modifier = Modifier
                    .haze(
                        state = hazeState,
                        style = HazeMaterials.thick(containerColor = White)
                    )
            ) {
                // api 호출도 토큰 있어야 가능해서.. 그냥 임시 데이터 넣어줌
                items(5) {
                    DummyImage()
                }
            }

            BlockNonUser(
                title = "사진",
                hazeState = hazeState
            )
        }
    }

    if (imageDetailInfo.isNotEmpty()) {
        ImageDetail(
            url = imageDetailInfo,
            onGoBack = { imageDetailInfo = "" }
        )
    }
}

@Composable
fun ImageDetail(
    url: String,
    onGoBack: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Black)
            .clickable { onGoBack() },
        contentAlignment = Alignment.Center
    ) {
        Image(
            painterResource(
                id = R.drawable.ic_close
            ),
            contentDescription = "",
            modifier = Modifier
                .align(Alignment.TopEnd)
                .clickable { onGoBack() }
                .padding(20.dp)
        )
        AsyncImage(
            model = url,
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
        )
    }
}

@Composable
fun BlockNonUser(
    title: String,
    hazeState: HazeState,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .hazeChild(
                state = hazeState,
                style = HazeStyle(
                    tint = White.copy(.2f),
                    blurRadius = 10.dp
                ) // 블러 스타일
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(
                    R.drawable.ic_empty_head,
                ),
                contentDescription = null,
                modifier = Modifier.size(50.dp)
            )
            Spacer(Modifier.height(4.dp))
            Text(
                "${title}이 궁금하다면?",
                style = AppTypography.Bold_20.copy(color = Black),
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(4.dp))
            Text(
                "로그인을 하면 리뷰, 사진, 즐겨찾기 등\n더 많은 기능을 이용할 수 있어요!",
                style = AppTypography.Medium_15.copy(color = Black),
                textAlign = TextAlign.Center
            )
        }
    }
}