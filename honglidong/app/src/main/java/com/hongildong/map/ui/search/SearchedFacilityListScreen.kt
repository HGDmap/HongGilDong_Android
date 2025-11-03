package com.hongildong.map.ui.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.hongildong.map.R
import com.hongildong.map.data.entity.NodeInfo
import com.hongildong.map.ui.home.RecommendPlaces
import com.hongildong.map.ui.search.direction.directions
import com.hongildong.map.ui.search.location_detail.SearchBarWithGoBack
import com.hongildong.map.ui.theme.AppTypography
import com.hongildong.map.ui.theme.Black
import com.hongildong.map.ui.theme.Gray300
import com.hongildong.map.ui.theme.Gray600
import com.hongildong.map.ui.util.ButtonWithIcon
import com.hongildong.map.ui.util.FlexibleBottomSheet
import com.hongildong.map.ui.util.map.MapViewmodel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchedFacilityListScreen(
    mapViewmodel: MapViewmodel,
    searchedWord: String,
    onDirect: () -> Unit,
    onGoBack: () -> Unit
) {
    val sheetScaffoldState = rememberBottomSheetScaffoldState()
    val nestedScrollConnection = rememberNestedScrollInteropConnection()

    Box(
        modifier = Modifier.background(Color.Transparent)
    ) {
        SearchBarWithGoBack(
            searchedWord = searchedWord,
            onGoBack = {
                onGoBack()
                mapViewmodel.clearMarker()
            }
        )

        FlexibleBottomSheet(
            modifier = Modifier
                .nestedScroll(
                    nestedScrollConnection
                ),
            sheetScaffoldState = sheetScaffoldState,
            isFullscreen = false
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top
            ) {
                // api 연결하기
                SearchedPlaces(directions)
            }
        }
    }
}

@Composable
fun SearchedPlaces(
    places: List<NodeInfo>
) {
    LazyColumn() {
        items(places) { place ->
            PlaceInfoItem(
                info = place,
                onDirect = {}
            )
        }
    }
}

@Composable
fun PlaceInfoItem(
    info: NodeInfo,
    onDirect: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),

        ) {
        Spacer(Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    info.name ?: "temp",
                    style = AppTypography.Medium_18.copy(color = Black)
                )
                Spacer(Modifier.height(3.dp))
                Text(
                    info.description ?: info.name ?: "",
                    style = AppTypography.Medium_13.copy(color = Gray600)
                )
            }
            Image(
                painterResource(
                    id = if (info.isBookmarked ?: false) R.drawable.ic_bookmark_true else R.drawable.ic_bookmark_false,
                ),
                contentDescription = "",
                modifier = Modifier
                    .clickable {
                        // todo: 북마크/취소 로직 작성하기
                    }
            )
        }
        Spacer(Modifier.height(12.dp))
        LazyRow (
            modifier = Modifier
                .height(120.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(info.images) { image ->
                // 네트워크 이미지 로더 추가 필요
                Image(
                    painterResource(R.drawable.img_blank),
                    contentDescription = "",
                    modifier = Modifier
                        .size(width = 110.dp, height = 90.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
            }
        }
        Spacer(Modifier.height(5.dp))
        ButtonWithIcon(
            icon = R.drawable.ic_direction,
            title = "길찾기",
            onClick = { onDirect() }
        )
        Spacer(Modifier.height(8.dp))
        HorizontalDivider(Modifier.height(1.dp), color = Gray300)
    }
}