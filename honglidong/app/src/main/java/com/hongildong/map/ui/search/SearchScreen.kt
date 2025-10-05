package com.hongildong.map.ui.search

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import androidx.navigation.NavHostController
import com.hongildong.map.R
import com.hongildong.map.ui.theme.AppTypography
import com.hongildong.map.ui.theme.White
import com.hongildong.map.ui.util.CustomTextField
import com.hongildong.map.ui.util.EmptyContents

@Composable
fun SearchScreen(
    navController: NavHostController,
    viewModel: SearchKeywordViewmodel = hiltViewModel<SearchKeywordViewmodel>(),
    onSearch: () -> Unit
) {
    var textState by remember { mutableStateOf("") }
    val recentlySearchedKeywords by viewModel.recentKeywords.collectAsState()

    Column(
        modifier = Modifier
            .background(White)
            .fillMaxSize()
            .padding(vertical = 15.dp, horizontal = 10.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .systemBarsPadding(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(R.drawable.ic_back),
                contentDescription = stringResource(R.string.go_back),
                modifier = Modifier
                    .clickable {
                        navController.popBackStack()
                    }
            )
            Spacer(
                modifier = Modifier.width(15.dp)
            )
            CustomTextField(
                placeholderMessage = stringResource(R.string.suggest_search),
                textState = textState,
                onTextChange = { textState = it },
                onSearch = {
                    viewModel.onSearch(it)
                    textState = ""
                    onSearch()
                },
                maxLength = 15
            )
        }

        Spacer(
            modifier = Modifier.height(15.dp)
        )

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
                        viewModel.clearAllKeyword()
                    }
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // 검색기록 없을 때는 검색기록이 없다 문구 / 검색기록 있을 때는 검색기록 보여주기
        if (recentlySearchedKeywords.isEmpty()) {
            // 검색기록 없음 문구
            EmptyContents(stringResource(R.string.empty_search_keyword))
        } else {
            // 검색기록 보여주기
            LazyColumn {
                items(recentlySearchedKeywords) { keyword ->
                    RecentlySearchedItem(
                        itemName = keyword.keyword,
                        onClickItem = {
                            textState = ""
                            viewModel.onSearch(keyword.keyword)
                            onSearch()
                        },
                        onDeleteItem = {
                            viewModel.deleteKeyword(keyword.keyword)
                        }
                    )
                }
            }
        }
    }
}