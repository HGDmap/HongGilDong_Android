package com.hongildong.map.ui.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.hongildong.map.navGraph.SearchNavHost
import com.hongildong.map.ui.util.map.MapBackground
import com.hongildong.map.ui.util.map.MapViewmodel

@Composable
fun SearchRootScreen(
    rootNavController: NavHostController
) {
    val navController = rememberNavController()
    val mapViewmodel: MapViewmodel = hiltViewModel()

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        MapBackground(
            viewModel = mapViewmodel,
            onClickBookmark = {}
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = paddingValues.calculateBottomPadding())
                .background(Color.Transparent)
        ) {
            SearchNavHost(
                rootNavController = rootNavController,
                searchNavController = navController,
                mapViewmodel = mapViewmodel
            )
        }
    }
}
