package com.hongildong.map.ui.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hongildong.map.R
import com.hongildong.map.ui.theme.AppTypography
import com.hongildong.map.ui.theme.Gray200
import com.hongildong.map.ui.theme.HongildongTheme

@Composable
fun SearchedItem(
    itemName: String,
    onClickItem: (String) -> Unit,
    onDeleteItem: (String) -> Unit,
    isRecentlySearched: Boolean = false
) {
    Column {
        Row (
            modifier = Modifier.fillMaxWidth().padding(horizontal = 3.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row (
                modifier = Modifier.weight(1f)
                    .clickable {
                        onClickItem(itemName)
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_location),
                    contentDescription = "",
                    modifier = Modifier.padding(end = 10.dp)
                )
                // 최근 검색된 검색어
                Text(
                    text = itemName,
                    style = AppTypography.Regular_15
                )
            }
            if (isRecentlySearched) {
                // 삭제 버튼
                Image(
                    painter = painterResource(R.drawable.ic_close),
                    contentDescription = stringResource(R.string.delete_item, itemName),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .padding(4.dp)
                        .size(12.dp)
                        .clickable { onDeleteItem(itemName) }
                )
            }
        }

        HorizontalDivider(
            thickness = 1.dp,
            color = Gray200
        )
    }
}

@Preview
@Composable
fun RecentlySearchedItemPreview() {
    HongildongTheme {
        Column {
            SearchedItem(
                itemName = "아이템1",
                onClickItem = {},
                onDeleteItem = {}
            )
            SearchedItem(
                itemName = "아이템2",
                onClickItem = {},
                onDeleteItem = {},
                isRecentlySearched = true
            )
        }
    }
}