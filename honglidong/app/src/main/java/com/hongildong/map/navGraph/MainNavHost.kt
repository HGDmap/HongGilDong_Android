package com.hongildong.map.navGraph

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.hongildong.map.ui.home.BookmarkScreen
import com.hongildong.map.ui.home.NearbyScreen
import com.hongildong.map.ui.home.ProfileScreen
import com.hongildong.map.ui.search.location_detail.LocationDetailScreen
import com.hongildong.map.ui.search.SearchKeywordViewmodel
import com.hongildong.map.ui.search.SearchScreen
import com.hongildong.map.ui.util.map.MapViewmodel

private const val SEARCH_GRAPH_ROUTE = "search_graph"

// 메인 화면 바텀 네비 호스트
@Composable
fun MainNavHost(
    rootNavController: NavHostController,
    mainNavController: NavHostController,
    mapViewmodel: MapViewmodel
) {
    // home
    NavHost(
        navController = mainNavController,
        startDestination = NavRoute.Nearby.route
    ) {

        composable(route = NavRoute.Nearby.route) {
            NearbyScreen(mainNavController)
        }
        composable(route = NavRoute.Bookmark.route) {
            BookmarkScreen(mainNavController)
        }
        composable(route = NavRoute.Profile.route) {
            ProfileScreen()
        }
        navigation(
            route = SEARCH_GRAPH_ROUTE,
            startDestination = NavRoute.Search.route
        ) {
            composable(route = NavRoute.Search.route) { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    mainNavController.getBackStackEntry(SEARCH_GRAPH_ROUTE)
                }
                val searchKeywordViewmodel: SearchKeywordViewmodel = hiltViewModel(parentEntry)

                SearchScreen(
                    mainNavController,
                    onSearch = {
                        mainNavController.navigate(NavRoute.LocationDetail.route)
                    },
                    viewModel = searchKeywordViewmodel
                )
            }
            composable(route = NavRoute.LocationDetail.route) { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    mainNavController.getBackStackEntry(SEARCH_GRAPH_ROUTE)
                }
                val searchKeywordViewmodel: SearchKeywordViewmodel = hiltViewModel(parentEntry)

                LocationDetailScreen(
                    mainNavController = mainNavController,
                    searchViewmodel = searchKeywordViewmodel,
                    mapViewmodel = mapViewmodel
                )
            }
        }

    }
}
