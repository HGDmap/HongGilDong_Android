package com.hongildong.map.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.hongildong.map.R
import com.hongildong.map.ui.theme.AppTypography
import com.hongildong.map.ui.util.FlexibleBottomSheet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NearbyScreen(
    navController: NavHostController,
) {
    val sheetScaffoldState = rememberBottomSheetScaffoldState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(),
        contentAlignment = Alignment.TopCenter
    ) {
        Column {
            SearchBar(navController)
            Spacer(Modifier.height(5.dp))
            FacilityTypeTags()
        }

        FlexibleBottomSheet(
            sheetScaffoldState = sheetScaffoldState
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top
            ) {
                Text(stringResource(R.string.place_recommend), style = AppTypography.Bold_20)
                Spacer(Modifier.height(20.dp))
                RecommendPlaces()
            }
        }

    }
}

@Composable
fun RecommendPlaces() {
    Text("recommend places", style = AppTypography.Regular_18)
}

@Composable
fun RecommendPlaceCard(place: Place) {
    Box(
        modifier = Modifier.fillMaxWidth()
    )
}

data class Place(
    val name: String = "멀티미디어실",
    val location: String = "제4공학관 T동 605호",
    val isBookmarked: Boolean = false,
    val images: List<Int> = listOf(R.drawable.img_blank * 15)
)
