package com.hongildong.map.ui.search.location_detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.hongildong.map.R
import com.hongildong.map.ui.search.RecentlySearchedKeywords
import com.hongildong.map.ui.search.SearchKeywordViewmodel
import com.hongildong.map.ui.theme.AppTypography
import com.hongildong.map.ui.theme.Black
import com.hongildong.map.ui.theme.Gray300
import com.hongildong.map.ui.theme.Gray400
import com.hongildong.map.ui.theme.White


@Composable
fun DirectionSearchScreen(
    searchViewmodel: SearchKeywordViewmodel = hiltViewModel<SearchKeywordViewmodel>(),
    onGoBack: () -> Unit,
    onDirect: () -> Unit
) {
    val departInfo by searchViewmodel.departPlaceInfo.collectAsState()
    val arrivalInfo by searchViewmodel.arrivalPlaceInfo.collectAsState()
    LaunchedEffect(key1 = departInfo, key2 = arrivalInfo) {
        // 두개 모두 채워질 경우 바로 direct 후 화면 이동
        // 이동 후에는 출발, 도착지 정보 삭제 - 뒤로가기시 무한경로검색을 막음
        if ((departInfo != null) and (arrivalInfo != null)) {
            searchViewmodel.direct()
            onDirect()
            searchViewmodel.deleteDepartAndArrivalData()
        }
    }

    Column (
        modifier = Modifier
            .background(White)
            .fillMaxSize()
            .systemBarsPadding()
            .padding(top = 16.dp, start = 10.dp, end = 10.dp, bottom = 16.dp),
    ) {
        DirectionHeader(
            depart = departInfo?.nodeName ?: "",
            arrival = arrivalInfo?.nodeName ?: "",
            onGoBack = {
                onGoBack()
                searchViewmodel.deleteDepartAndArrivalData()
            },
        )
        Spacer(Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.recently_searched_title),
                style = AppTypography.Bold_18
            )

            Text(
                text = stringResource(R.string.delete_all),
                style = AppTypography.Regular_13,
                modifier = Modifier
                    .padding(horizontal = 10.dp, vertical = 5.dp)
                    .clickable {
                        // 최근 검색 기록 전체 삭제
                        searchViewmodel.clearAllKeyword()
                    }
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        RecentlySearchedKeywords(
            viewModel = searchViewmodel,
            onSearch = {
                if (departInfo == null) {
                    searchViewmodel.setDepart(it)
                } else if (arrivalInfo == null) {
                    searchViewmodel.setArrival(it)
                }
            }
        )

    }

}

@Composable
fun DirectionHeader(
    modifier: Modifier = Modifier,
    depart: String = "",
    arrival: String = "",
    onGoBack: () -> Unit,
    onSetDepart: () -> Unit = {},
    onSetArrival: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .background(White)
            .fillMaxWidth()
            .border(width = 1.dp, shape = RoundedCornerShape(20.dp), color = Gray400)
            .padding(horizontal = 15.dp, vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically

    ) {
        Image(
            painter = painterResource(R.drawable.ic_switch),
            contentDescription = "switch",
        )
        Spacer(Modifier.width(10.dp))
        Column {
            IconWithNodeName(
                icon = R.drawable.ic_location_depart,
                title = depart,
                titlePlaceholder = "출발지를 입력하세요",
                withClose = true,
                onGoBack = onGoBack,
                onChangeTitle = onSetDepart
            )
            HorizontalDivider(thickness = 1.dp, color = Gray300)
            IconWithNodeName(
                icon =R.drawable.ic_location_arrival,
                title = arrival,
                titlePlaceholder = "도착지를 입력하세요",
                onChangeTitle = onSetArrival
            )
        }
    }
   /* Column (
        modifier = Modifier
            .shadow(3.dp)
            .background(White)
            .wrapContentSize()
            .systemBarsPadding()
            .padding(top = 16.dp, start = 10.dp, end = 10.dp, bottom = 16.dp),
    ) {

    }*/
}

@Composable
fun IconWithNodeName(
    icon: Int,
    title: String = "",
    titlePlaceholder: String,
    withClose: Boolean = false,
    onGoBack: () -> Unit = {},
    onChangeTitle: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(icon),
            contentDescription = null,
            modifier = Modifier
                .padding(horizontal = 5.dp)
        )
        Text(
            text = title.ifEmpty { titlePlaceholder },
            style = AppTypography.Medium_18
                .copy(color = if (title.isEmpty()) Gray400 else Black),
            modifier = Modifier
                .padding(start = 16.dp)
                .weight(1f)
                .clickable {
                    onChangeTitle()
                }
        )
        if (withClose) {
            Image(
                painter = painterResource(R.drawable.ic_close),
                contentDescription = "검색 취소하기",
                modifier = Modifier
                    .size(26.dp)
                    .padding(horizontal = 5.dp)
                    .clickable { onGoBack() }
            )
        }
    }
}