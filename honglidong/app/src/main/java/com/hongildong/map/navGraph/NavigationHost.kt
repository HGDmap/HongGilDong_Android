package com.hongildong.map.navGraph

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.hongildong.map.ui.MainScreen

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

        navigation(
            route = NavRoute.MainFlow.route,
            startDestination = NavRoute.Main.route
        ) {
            composable(NavRoute.Main.route) {
                MainScreen(
                    rootNavController = rootNavController
                )
            }
        }
    }
}
