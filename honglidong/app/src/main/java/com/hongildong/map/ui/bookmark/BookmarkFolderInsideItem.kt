package com.hongildong.map.ui.bookmark

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.hongildong.map.R
import com.hongildong.map.data.entity.BookmarkInfo
import com.hongildong.map.ui.home.Place
import com.hongildong.map.ui.theme.AppTypography
import com.hongildong.map.ui.theme.Black
import com.hongildong.map.ui.theme.Gray300
import com.hongildong.map.ui.theme.Gray600

@Composable
fun BookmarkFolderInsideItem(
    bookmark: BookmarkInfo,
    onModifyBookmark: () -> Unit,
    onClickBookmark: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClickBookmark()
            },
        ) {
        Spacer(Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    bookmark.name,
                    style = AppTypography.Medium_18.copy(color = Black)
                )
                Spacer(Modifier.height(3.dp))
                Text(
                    bookmark.location,
                    style = AppTypography.Medium_13.copy(color = Gray600)
                )
            }
            Image(
                painterResource(
                    id = R.drawable.ic_bookmark_true
                ),
                contentDescription = "",
                modifier = Modifier
                    .clickable {
                        onModifyBookmark()
                    }
            )
        }
        Spacer(Modifier.height(12.dp))
        LazyRow (
            modifier = Modifier
                .height(120.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                // todo: bookmark.images
                3
            ) { image ->
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
        //Spacer(Modifier.height(8.dp))
        HorizontalDivider(Modifier.height(1.dp), color = Gray300)
    }
}