package com.hongildong.map.navGraph

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.hongildong.map.ui.search.SearchKeywordViewmodel
import com.hongildong.map.ui.search.SearchScreen
import com.hongildong.map.ui.search.location_detail.DirectionScreen
import com.hongildong.map.ui.search.location_detail.LocationDetailScreen
import com.hongildong.map.ui.util.map.MapViewmodel


// SearchNavHost 내부에서 사용할 그래프의 route 이름
private const val SEARCH_GRAPH_ROUTE = "search_graph"

@Composable
fun SearchNavHost(
    rootNavController: NavHostController, // 상위(AppNavHost)로 돌아가기 위한 NavController
    searchNavController: NavHostController,
    mapViewmodel: MapViewmodel = hiltViewModel<MapViewmodel>()
) {
    NavHost(
        navController = searchNavController,
        startDestination = SEARCH_GRAPH_ROUTE
    ) {
        navigation(
            route = SEARCH_GRAPH_ROUTE,
            startDestination = NavRoute.Search.route
        ) {
            composable(route = NavRoute.Search.route) { backStackEntry ->
                // SEARCH_GRAPH_ROUTE를 찾아 ViewModel을 공유
                val parentEntry = remember(backStackEntry) {
                    searchNavController.getBackStackEntry(SEARCH_GRAPH_ROUTE)
                }
                val searchKeywordViewmodel: SearchKeywordViewmodel = hiltViewModel(parentEntry)

                SearchScreen(
                    onSearch = { keyword ->
                        searchNavController.navigate(NavRoute.LocationDetail.route + "/$keyword")
                    },
                    onGoBack = {
                        rootNavController.navigate(NavRoute.MainFlow.route) {
                            popUpTo(NavRoute.SearchFlow.route) { inclusive = true }
                        }
                    },
                    viewModel = searchKeywordViewmodel
                )
            }
            composable(route = NavRoute.LocationDetail.route + "/{searchedWord}") { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    searchNavController.getBackStackEntry(SEARCH_GRAPH_ROUTE)
                }
                val searchKeywordViewmodel: SearchKeywordViewmodel = hiltViewModel(parentEntry)
                val searchedWord = backStackEntry.arguments?.getString("searchedWord") ?: ""

                LocationDetailScreen(
                    searchedWord = searchedWord,
                    searchViewmodel = searchKeywordViewmodel,
                    mapViewmodel = mapViewmodel,
                    onGoBack = {
                        searchNavController.popBackStack()
                    },
                    onDepart = {
                        searchNavController.navigate(NavRoute.Direction.route + "/from:$it&to:")
                    },
                    onArrival = {
                        searchNavController.navigate(NavRoute.Direction.route + "/from:&to:$it")
                    }
                )
            }
            composable(route = NavRoute.Direction.route + "/from:{depart}&to:{arrival}") { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    searchNavController.getBackStackEntry(SEARCH_GRAPH_ROUTE)
                }
                val searchKeywordViewmodel: SearchKeywordViewmodel = hiltViewModel(parentEntry)
                val departPlace = backStackEntry.arguments?.get("depart") ?: ""
                val arrivalPlace = backStackEntry.arguments?.getString("arrival") ?: ""

                DirectionScreen(
                    departPlace = departPlace,
                    arrivalPlace = arrivalPlace,
                    searchViewmodel = searchKeywordViewmodel,
                    onGoBack = {searchNavController.popBackStack()}
                )
            }
        }
    }
}
