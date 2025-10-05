package com.hongildong.map.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.hongildong.map.R
import com.hongildong.map.navGraph.NavRoute
import com.hongildong.map.ui.theme.AppTypography
import com.hongildong.map.ui.theme.Gray400
import com.hongildong.map.ui.theme.White

@Composable
fun SearchBar(
    navController: NavHostController,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(top = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .shadow(6.dp)
                .clickable {
                    navController.navigate(NavRoute.Search.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
                .background(color = White, shape = RoundedCornerShape(size = 10.dp))
                .padding(all = 15.dp)
        ) {
            Text(
                stringResource(R.string.suggest_search),
                style = AppTypography.Regular_15.copy(color = Gray400)
            )
        }
        Spacer(Modifier.width(10.dp))
        Image(
            painter = painterResource(R.drawable.ic_search_route),
            contentDescription = stringResource(R.string.search_button),
            modifier = Modifier
                .fillMaxWidth(1f)
                .height(56.dp)
                .shadow(6.dp)
                .clickable {
                    navController.navigate(NavRoute.Search.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
        )
    }
}

enum class Tag(
    val title: String,
    val icon: Int,
) {
    EVENT("이벤트", R.drawable.img_event_color),
    CAFE("카페", R.drawable.img_cafe_color),
    // todo: 태그 추가해야함
}

@Composable
fun Tags() {
    val tags = Tag.entries
    LazyRow (
        modifier = Modifier
            .fillMaxWidth()
    ) {
        items(tags) { tag ->
            TagItem(tag.icon, tag.title)
        }
    }
}

@Composable
fun TagItem(
    tagIcon: Int,
    tagTitle: String,
) {
    Box(
        modifier = Modifier
            .padding(5.dp)
            .wrapContentSize()
            .shadow(6.dp)
            .clickable {}
            .background(color = White, shape = RoundedCornerShape(size = 20.dp))
            .padding(vertical = 8.dp, horizontal = 12.dp)
    ) {
        Row (
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(tagIcon),
                contentDescription = tagTitle,
                modifier = Modifier.size(18.dp)
            )
            Spacer(Modifier.width(6.dp))
            Text(
                tagTitle,
                style = AppTypography.Medium_15
            )
        }
    }
}
