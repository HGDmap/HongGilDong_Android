package com.hongildong.map.ui.home.search

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.room.util.copy
import com.hongildong.map.R
import com.hongildong.map.data.entity.SearchKeyword
import com.hongildong.map.ui.theme.Gray600
import com.hongildong.map.ui.theme.HongildongTheme
import com.hongildong.map.ui.theme.White
import com.hongildong.map.ui.util.CustomTextField
import com.hongildong.map.ui.util.EmptyContents
import dagger.hilt.android.AndroidEntryPoint

@Composable
fun SearchScreen(
    navController: NavHostController,
    viewModel: SearchKeywordViewmodel = hiltViewModel<SearchKeywordViewmodel>()
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
                contentDescription = "go back button",
                modifier = Modifier
                    .clickable {
                        navController.popBackStack()
                    }
            )
            CustomTextField(
                placeholderMessage = "홍익대학교에서 길을 찾아보세요.",
                textState = textState,
                onTextChange = { textState = it },
                onSearch = {
                    viewModel.onSearch(it)
                    textState = ""
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
                text = "최근 검색",
                style = MaterialTheme.typography.titleSmall
            )

            Text(
                text = "전체 삭제",
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Normal),
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
            EmptyContents("검색 기록이 없습니다.")
        } else {
            // 검색기록 보여주기
            LazyColumn {
                items(recentlySearchedKeywords) { keyword ->
                    RecentlySearchedItem(
                        itemName = keyword.keyword,
                        onClickItem = {
                            textState = ""
                            viewModel.onSearch(keyword.keyword)
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