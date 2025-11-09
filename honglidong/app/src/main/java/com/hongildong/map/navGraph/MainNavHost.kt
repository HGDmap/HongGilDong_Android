package com.hongildong.map.navGraph

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.hongildong.map.ui.home.BookmarkScreen
import com.hongildong.map.ui.home.NearbyScreen
import com.hongildong.map.ui.home.ProfileScreen
import com.hongildong.map.ui.util.bottomsheet.BottomSheetViewModel
import com.hongildong.map.ui.util.map.MapViewmodel


// 메인 화면 바텀 네비 호스트
@Composable
fun MainNavHost(
    rootNavController: NavHostController,
    mainNavController: NavHostController,
    mapViewmodel: MapViewmodel,
    bottomSheetViewModel: BottomSheetViewModel
) {
    // home
    NavHost(
        navController = mainNavController,
        startDestination = NavRoute.Nearby.route
    ) {

        composable(route = NavRoute.Nearby.route) {
            NearbyScreen(
                onSearch = {
                    rootNavController.navigate(NavRoute.SearchFlow.route) {
                        popUpTo(mainNavController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
        composable(route = NavRoute.Bookmark.route) {
            BookmarkScreen(
                onSearch = {
                    rootNavController.navigate(NavRoute.SearchFlow.route) {
                        popUpTo(mainNavController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                bottomSheetViewModel = bottomSheetViewModel
            )
        }
        composable(route = NavRoute.Profile.route) {
            ProfileScreen()
        }

    }
}
