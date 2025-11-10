package com.hongildong.map.navGraph

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.hongildong.map.ui.MainScreen
import com.hongildong.map.ui.bookmark.BookmarkViewModel
import com.hongildong.map.ui.search.SearchRootScreen
import com.hongildong.map.ui.search.SearchScreen
import com.hongildong.map.ui.util.map.MapViewmodel

const val WHOLE_APP_ROUTE = "whole_app"

// 전체 앱 navhost
@Composable
fun AppNavHost() {
    val rootNavController = rememberNavController()

    NavHost(
        navController = rootNavController,
        startDestination = NavRoute.EnterFlow.route
    ) {
        navigation(
            route = NavRoute.EnterFlow.route,
            startDestination = NavRoute.Enter.route
        ) {
            composable(NavRoute.Enter.route) {
                EnterNavHost(rootNavController = rootNavController)
            }
        }

        navigation (
            route = WHOLE_APP_ROUTE,
            startDestination = NavRoute.MainFlow.route
        ) {
            navigation(
                route = NavRoute.MainFlow.route,
                startDestination = NavRoute.Main.route
            ) {
                composable(NavRoute.Main.route) { backStackEntry ->
                    val parentEntry = remember(backStackEntry) {
                        rootNavController.getBackStackEntry(WHOLE_APP_ROUTE)
                    }
                    val mapViewmodel: MapViewmodel = hiltViewModel(parentEntry)
                    val bookmarkViewModel: BookmarkViewModel = hiltViewModel(parentEntry)

                    MainScreen(
                        rootNavController = rootNavController,
                        mapViewModel = mapViewmodel,
                        bookmarkViewModel = bookmarkViewModel
                    )
                }
            }

            navigation(
                route = NavRoute.SearchFlow.route,
                startDestination = NavRoute.SearchRoot.route
            ) {
                composable(NavRoute.SearchRoot.route) { backStackEntry ->
                    val parentEntry = remember(backStackEntry) {
                        rootNavController.getBackStackEntry(WHOLE_APP_ROUTE)
                    }
                    val mapViewmodel: MapViewmodel = hiltViewModel(parentEntry)
                    val bookmarkViewModel: BookmarkViewModel = hiltViewModel(parentEntry)

                    SearchRootScreen(
                        rootNavController = rootNavController,
                        mapViewModel = mapViewmodel,
                        bookmarkViewModel = bookmarkViewModel
                    )
                }
            }
        }
    }
}
