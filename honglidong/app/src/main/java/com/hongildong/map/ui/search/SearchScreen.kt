package com.hongildong.map.ui.search

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
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
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.hongildong.map.R
import com.hongildong.map.data.entity.SearchKeyword
import com.hongildong.map.data.entity.toSearchKeyword
import com.hongildong.map.navGraph.LOCATION_SEARCH_MODE
import com.hongildong.map.ui.theme.AppTypography
import com.hongildong.map.ui.theme.White
import com.hongildong.map.ui.util.CustomTextField
import com.hongildong.map.ui.util.EmptyContents

@Composable
fun SearchScreen(
    viewModel: SearchKeywordViewmodel = hiltViewModel<SearchKeywordViewmodel>(),
    onSearch: (SearchKeyword) -> Unit,
    onRawSearch: (String) -> Unit,
    onGoBack: () -> Unit,
    searchMode: String,
) {
    var textState by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier
            .background(White)
            .fillMaxSize()
            .padding(vertical = 15.dp, horizontal = 10.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.statusBars),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(R.drawable.ic_back),
                contentDescription = stringResource(R.string.go_back),
                modifier = Modifier
                    .clickable {
                        onGoBack()
                    }
            )
            Spacer(
                modifier = Modifier.width(15.dp)
            )
            CustomTextField(
                placeholderMessage = stringResource(R.string.suggest_search),
                textState = textState,
                onTextChange = {
                    textState = it
                    viewModel.autoCompleteSearch(it)
                },
                onSearch = {
                    when (searchMode) {
                        // 장소 검색 모드일 경우 키보드로 직접 검색 -> 검색 호출
                        LOCATION_SEARCH_MODE -> {
                            if (textState == "") {
                                viewModel.onSearchRawWord(textState)
                                onRawSearch(textState)
                                textState = ""
                            } else {
                                viewModel.onSearchRawWord(textState)
                            }
                        }
                        // 경로 검색 모드일 경우 자동완성된 리스트에서 고르도록 유도 -> 키보드 숨김
                        else -> {
                            keyboardController?.hide()
                        }
                    }
                },
                maxLength = 15
            )
        }

        Spacer(
            modifier = Modifier.height(15.dp)
        )

        if (textState.isEmpty()) {
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
        }

        if (textState.isEmpty()) {
            RecentlySearchedKeywords(
                viewModel = viewModel,
                onSearch = {
                    onSearch(it)
                    textState = ""
                }
            )
        } else {
            AutoCompleteSearchedKeyword(
                viewModel = viewModel,
                onSearch = {
                    onSearch(it)
                    textState = ""
                }
            )
        }
    }
}

@Composable
fun AutoCompleteSearchedKeyword(
    viewModel: SearchKeywordViewmodel = hiltViewModel<SearchKeywordViewmodel>(),
    onSearch: (SearchKeyword) -> Unit
) {
    val autoCompleteResult by viewModel.autoCompleteResult.collectAsState()

    LazyColumn {
        items(autoCompleteResult) { searchedResult ->
            val keyword = searchedResult.toSearchKeyword()

            SearchedItem(
                itemName = keyword.nodeName,
                onClickItem = {
                    viewModel.onSearch(keyword)
                    onSearch(keyword)
                },
                onDeleteItem = { },
                isRecentlySearched = false
            )
        }
    }
}

@Composable
fun RecentlySearchedKeywords(
    viewModel: SearchKeywordViewmodel = hiltViewModel<SearchKeywordViewmodel>(),
    onSearch: (SearchKeyword) -> Unit,
) {
    val recentlySearchedKeywords by viewModel.recentKeywords.collectAsState()

    // 검색기록 없을 때는 검색기록이 없다 문구 / 검색기록 있을 때는 검색기록 보여주기
    if (recentlySearchedKeywords.isEmpty()) {
        // 검색기록 없음 문구
        EmptyContents(stringResource(R.string.empty_search_keyword))
    } else {
        // 검색기록 보여주기
        LazyColumn {
            items(recentlySearchedKeywords) { keyword ->
                SearchedItem(
                    itemName = keyword.nodeName,
                    onClickItem = {
                        viewModel.onSearch(keyword)
                        onSearch(keyword)
                    },
                    onDeleteItem = {
                        viewModel.deleteKeyword(keyword)
                    },
                    isRecentlySearched = true
                )
            }
        }
    }
}