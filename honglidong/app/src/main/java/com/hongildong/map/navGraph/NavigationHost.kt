package com.hongildong.map.navGraph

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.hongildong.map.ui.MainScreen
import com.hongildong.map.ui.home.BookmarkScreen
import com.hongildong.map.ui.home.NearbyScreen
import com.hongildong.map.ui.home.ProfileScreen
import com.hongildong.map.ui.home.search.SearchScreen
import com.hongildong.map.ui.user.EnterScreen
import com.hongildong.map.ui.user.LoginScreen
import com.hongildong.map.ui.user.SignupScreen

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

// 로그인/회원가입 부분 navhost
@Composable
fun EnterNavHost(
    rootNavController: NavHostController
) {
    val enterNavController = rememberNavController()

    NavHost(
        navController = enterNavController,
        startDestination = NavRoute.Enter.route
    ) {
        composable(NavRoute.Enter.route) {
            EnterScreen(
                onLoginClick = {
                    enterNavController.navigate(NavRoute.Login.route)
                },
                onStrangerClick = {
                    // 메인으로 이동
                    rootNavController.navigate(NavRoute.MainFlow.route) {
                        popUpTo(NavRoute.EnterFlow.route) { inclusive = true }
                    }
                }
            )
        }
        composable(NavRoute.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    rootNavController.navigate(NavRoute.MainFlow.route) {
                        popUpTo(NavRoute.EnterFlow.route) { inclusive = true }
                    }
                },
                onGoBackClick = {
                    enterNavController.popBackStack()
                },
                onGoSignupClick = {
                    enterNavController.navigate(NavRoute.Signup.route)
                }
            )
        }
        composable(NavRoute.Signup.route) {
            SignupScreen(
                onGoBackClick = {
                    enterNavController.popBackStack()
                },
                onSignupClick = {
                    enterNavController.popBackStack()
                }
            )
        }
    }
}

// 메인 화면 바텀 네비 호스트
@Composable
fun MainNavHost(
    rootNavController: NavHostController,
    mainNavController: NavHostController
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
        composable(route = NavRoute.Search.route) {
            SearchScreen(mainNavController)
        }
    }
}

sealed class NavRoute(val route: String) {
    // flow
    object EnterFlow: NavRoute("enter_flow")
    object MainFlow: NavRoute("main_flow")

    // home
    object Nearby : NavRoute("nearby")
    object Bookmark : NavRoute("bookmark")
    object Profile : NavRoute("profile")
    object Search : NavRoute("search")
    object Main: NavRoute("main")

    // user
    object Enter: NavRoute("enter")
    object Login: NavRoute("login")
    object Signup: NavRoute("signup")

}
