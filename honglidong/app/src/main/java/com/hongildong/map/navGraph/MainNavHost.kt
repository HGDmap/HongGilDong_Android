package com.hongildong.map.navGraph

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.hongildong.map.ui.bookmark.BookmarkViewModel
import com.hongildong.map.ui.home.BookmarkScreen
import com.hongildong.map.ui.home.NearbyScreen
import com.hongildong.map.ui.home.ProfileScreen
import com.hongildong.map.ui.util.bottomsheet.BottomSheetViewModel
import com.hongildong.map.ui.util.map.MapViewmodel

private const val MAIN_GRAPH_ROUTE = "main_graph"

// 메인 화면 바텀 네비 호스트
@Composable
fun MainNavHost(
    rootNavController: NavHostController,
    mainNavController: NavHostController,
    mapViewmodel: MapViewmodel,
    bookmarkViewModel: BookmarkViewModel,
    bottomSheetViewModel: BottomSheetViewModel
) {
    // home
    NavHost(
        navController = mainNavController,
        startDestination = MAIN_GRAPH_ROUTE
    ) {
        // 뷰모델 공유를 위한 공유 스코프
        navigation(
            route = MAIN_GRAPH_ROUTE,
            startDestination = NavRoute.Nearby.route
        ) {
            composable(route = NavRoute.Nearby.route) { backStackEntry ->
                // MAIN_GRAPH_ROUTE를 찾아 ViewModel을 공유
                val parentEntry = remember(backStackEntry) {
                    mainNavController.getBackStackEntry(MAIN_GRAPH_ROUTE)
                }
                //val bookmarkViewmodel: BookmarkViewModel = hiltViewModel(parentEntry)

                NearbyScreen(
                    onSearch = {
                        rootNavController.navigate(NavRoute.SearchFlow.route()) {
                            popUpTo(mainNavController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    bookmarkViewModel = bookmarkViewModel,
                    mapViewModel = mapViewmodel
                )
            }
            composable(route = NavRoute.Bookmark.route) { backStackEntry ->
                // MAIN_GRAPH_ROUTE를 찾아 ViewModel을 공유
                val parentEntry = remember(backStackEntry) {
                    mainNavController.getBackStackEntry(MAIN_GRAPH_ROUTE)
                }
                //val bookmarkViewmodel: BookmarkViewModel = hiltViewModel(parentEntry)

                BookmarkScreen(
                    onSearch = {
                        rootNavController.navigate(NavRoute.SearchFlow.route()) {
                            popUpTo(mainNavController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    bottomSheetViewModel = bottomSheetViewModel,
                    bookmarkViewModel = bookmarkViewModel
                )
            }
            composable(route = NavRoute.Profile.route) {
                ProfileScreen()
            }
        }
    }
}
