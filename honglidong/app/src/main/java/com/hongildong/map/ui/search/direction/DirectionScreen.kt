package com.hongildong.map.ui.search.direction

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.hongildong.map.R
import com.hongildong.map.data.entity.NodeInfo
import com.hongildong.map.ui.search.SearchKeywordViewmodel
import com.hongildong.map.ui.theme.AppTypography
import com.hongildong.map.ui.theme.Black
import com.hongildong.map.ui.theme.Gray400
import com.hongildong.map.ui.theme.PrimaryMid
import com.hongildong.map.ui.theme.White
import com.hongildong.map.ui.util.FlexibleBottomSheet
import com.hongildong.map.ui.util.map.MapViewmodel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DirectionScreen(
    searchViewmodel: SearchKeywordViewmodel,
    mapViewmodel: MapViewmodel,
    onGoBack: () -> Unit,
) {
    val departInfo by searchViewmodel.departPlaceInfo.collectAsState()
    val arrivalInfo by searchViewmodel.arrivalPlaceInfo.collectAsState()
    val directionInfo by searchViewmodel.directionResult.collectAsState()

    var calculatedMin by remember { mutableIntStateOf(0) }
    LaunchedEffect(departInfo, arrivalInfo, directionInfo) {
        directionInfo?.let {
            // 길찾기 정보가 검색 완료되었다면
            // 지도에 그리기
            mapViewmodel.showPath(directionInfo!!.nodes)

            // 소요시간 분단위(반올림해서) 계산
            calculatedMin = directionInfo!!.minute
            // 초는 반올림
            if (directionInfo!!.seconds >= 30) calculatedMin++
        }
    }

    val sheetScaffoldState = rememberBottomSheetScaffoldState()
    val nestedScrollConnection = rememberNestedScrollInteropConnection()

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        Box(
            modifier = Modifier
                .shadow(3.dp)
                .background(White)
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.statusBars)
                .padding(top = 16.dp, start = 10.dp, end = 10.dp, bottom = 16.dp),
        ) {
            DirectionHeader(
                depart = departInfo?.nodeName ?: "",
                arrival = arrivalInfo?.nodeName ?: "",
                onGoBack = {
                    onGoBack()
                },
                onClick = {
                    onGoBack()
                },
                isSearchScreen = false
            )
        }

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
                DirectionSheetHeader(calculatedMin)
                Spacer(Modifier.height(10.dp))
                DirectionSheetContent(directionInfo?.nodes ?: directions)
            }
        }

    }
}


// 경로명과 소요 시간(반올림한 분 단위)
@Composable
fun DirectionSheetHeader(
    minute: Int
) {
    Row (
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "가장 빠른 경로",
            style = AppTypography.Medium_18.copy(color = PrimaryMid)
        )
        Text(
            text = "${minute}분",
            style = AppTypography.Medium_32.copy(color = PrimaryMid)
        )
    }

}


@Composable
fun DirectionSheetContent(
    nodes: List<NodeInfo>
) {

    LazyColumn {
        items(nodes) { node ->
            DirectionSheetContentItem(node)
        }
    }
}


@Composable
fun DirectionSheetContentItem(
    content: NodeInfo
) {
    Column {
        Row (
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 10.dp)
        ) {
            // todo: 이후 타입별로 아이콘 따로해야되면 수정
            Image(
                painter = painterResource(R.drawable.ic_direction_info),
                contentDescription = null,
                modifier = Modifier.size(32.dp)
            )
            Text(
                text = content.nodeName ?: "temp",
                style = AppTypography.Bold_18.copy(color = Black),
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
            )

            // todo: 이후 s3 서버 연결되면 coil 이미지로딩으로 바꾸기
            Image(
                painterResource(R.drawable.img_blank),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(90.dp, 60.dp)
                    .clip(RoundedCornerShape(5.dp))
            )
        }
        HorizontalDivider(
            thickness = 1.dp,
            color = Gray400
        )
    }
}