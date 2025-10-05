package com.hongildong.map.navGraph

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.hongildong.map.ui.user.EnterScreen
import com.hongildong.map.ui.user.LoginScreen
import com.hongildong.map.ui.user.signup.AuthViewmodel
import com.hongildong.map.ui.user.signup.EmailEnterScreen
import com.hongildong.map.ui.user.signup.PasswordEnterScreen
import com.hongildong.map.ui.user.signup.TermsAgreementScreen
import com.hongildong.map.ui.user.signup.TermsDetailScreen
import com.hongildong.map.ui.user.signup.UserInfoEnterScreen


private const val SIGNUP_GRAPH_ROUTE = "signup_graph"

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
                    // 변경: 선택 약관 삭제되며 1개의 약관 전체를 보여주는 것으로 플로우 변경
                    //enterNavController.navigate(NavRoute.TermsAgreement.route)
                    enterNavController.navigate(NavRoute.TermsDetail.route + "/0")
                }
            )
        }

        composable(NavRoute.TermsAgreement.route) {
            TermsAgreementScreen (
                onGoBackClick = {
                    enterNavController.popBackStack()
                },
                onNextClick = {
                    enterNavController.popBackStack()
                },
                onShowDetailClick = {
                    enterNavController.navigate(NavRoute.TermsDetail.route + "/${it}")
                }
            )
        }

        composable(NavRoute.TermsDetail.route + "/{termId}") { backStackEntry ->
            TermsDetailScreen(
                termId = backStackEntry.arguments?.getInt("termId") ?: 0,
                onGoBackClick = {
                    enterNavController.popBackStack()
                },
                onAgreeClick = {
                    //enterNavController.popBackStack()
                    enterNavController.navigate(NavRoute.EmailEnter.route)
                }
            )
        }

        // 뷰모델 공유를 위한 공유 스코프
        navigation(
            route = SIGNUP_GRAPH_ROUTE,
            startDestination = NavRoute.EmailEnter.route
        ) {
            composable(NavRoute.EmailEnter.route) { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    enterNavController.getBackStackEntry(SIGNUP_GRAPH_ROUTE)
                }
                val authViewmodel: AuthViewmodel = hiltViewModel(parentEntry)

                EmailEnterScreen(
                    onGoBackClick = {
                        enterNavController.popBackStack()
                    },
                    onNextClick = {
                        enterNavController.navigate(NavRoute.PasswordEnter.route)
                    },
                    authViewmodel = authViewmodel,
                )
            }
            composable(NavRoute.PasswordEnter.route) { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    enterNavController.getBackStackEntry(SIGNUP_GRAPH_ROUTE)
                }
                val authViewmodel: AuthViewmodel = hiltViewModel(parentEntry)

                PasswordEnterScreen(
                    onGoBackClick = {
                        enterNavController.popBackStack()
                    },
                    onNextClick = {
                        enterNavController.navigate(NavRoute.UserInfoEnter.route)
                    },
                    authViewmodel = authViewmodel,
                )
            }
            composable(NavRoute.UserInfoEnter.route) { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    enterNavController.getBackStackEntry(SIGNUP_GRAPH_ROUTE)
                }
                val authViewmodel: AuthViewmodel = hiltViewModel(parentEntry)

                UserInfoEnterScreen(
                    onGoBackClick = {
                        enterNavController.popBackStack()
                    },
                    onSignupClick = {
                        // todo: 회원 가입 후 자동로그인 할건지? 아니면 로그인으로 돌아갈지?
                        // 일단 로그인으로 돌아가게 만듦
                        enterNavController.navigate(NavRoute.Login.route) {
                            popUpTo(NavRoute.Login.route) { inclusive = true }
                        }
                    },
                    authViewmodel = authViewmodel,
                )
            }
        }
    }
}
