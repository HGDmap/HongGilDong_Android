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
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.hongildong.map.ui.theme.Gray300
import com.hongildong.map.ui.theme.Gray400
import com.hongildong.map.ui.theme.White
import com.hongildong.map.ui.util.CustomInfixIconTextField


@Composable
fun DirectionScreen(
    departPlace: String = "",
    arrivalPlace: String = "",
    searchViewmodel: SearchKeywordViewmodel = hiltViewModel<SearchKeywordViewmodel>(),
    onGoBack: () -> Unit
) {
    var departState by remember { mutableStateOf("") }
    var arrivalState by remember { mutableStateOf("") }

    Column (
        modifier = Modifier
            .background(White)
            .fillMaxSize()
            .systemBarsPadding()
            .padding(top = 16.dp, start = 10.dp, end = 10.dp, bottom = 16.dp),
    ) {
        Row(
            modifier = Modifier
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
                CustomInfixIconTextField(
                    placeholderMessage = "출발지를 입력하세요",
                    textState = departState,
                    onTextChange = { departState = it },
                    onEditDone = { },
                    onGoBack = { onGoBack() },
                    icon = R.drawable.ic_location_depart,
                    isWithClose = true
                )
                HorizontalDivider(thickness = 1.dp, color = Gray300)
                CustomInfixIconTextField(
                    placeholderMessage = "도착지를 입력하세요",
                    textState = arrivalState,
                    onTextChange = { arrivalState = it },
                    onEditDone = { },
                    icon = R.drawable.ic_location_arrival
                )
            }
        }

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
                if (departState.isEmpty()) departState = it
                else arrivalState = it
            }
        )

    }

}
