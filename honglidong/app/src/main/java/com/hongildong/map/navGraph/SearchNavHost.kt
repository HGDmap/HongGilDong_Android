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
import com.hongildong.map.ui.search.direction.DirectionScreen
import com.hongildong.map.ui.search.direction.DirectionSearchScreen
import com.hongildong.map.ui.search.location_detail.LocationDetailScreen
import com.hongildong.map.ui.util.map.MapViewmodel

const val LOCATION_SEARCH_MODE = "location"
const val DIRECTION_SEARCH_MODE_FROM = "direction_from"
const val DIRECTION_SEARCH_MODE_TO = "direction_to"

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
            startDestination = NavRoute.Search.route + "/$LOCATION_SEARCH_MODE"
        ) {
            composable(route = NavRoute.Search.route + "/{searchMode}") { backStackEntry ->
                // SEARCH_GRAPH_ROUTE를 찾아 ViewModel을 공유
                val parentEntry = remember(backStackEntry) {
                    searchNavController.getBackStackEntry(SEARCH_GRAPH_ROUTE)
                }
                val searchKeywordViewmodel: SearchKeywordViewmodel = hiltViewModel(parentEntry)
                val searchMode = backStackEntry.arguments?.getString("searchMode") ?: LOCATION_SEARCH_MODE

                SearchScreen(
                    onSearch = { keyword ->
                        when (searchMode) {
                            LOCATION_SEARCH_MODE -> {
                                searchNavController.navigate(NavRoute.LocationDetail.route + "/${keyword.nodeName}")
                            }
                            DIRECTION_SEARCH_MODE_FROM -> {
                                // viewmodel에 출발지 저장
                                searchKeywordViewmodel.setDepart(keyword)
                                // 경로 검색화면으로 되돌아가기
                                searchNavController.popBackStack()
                            }
                            DIRECTION_SEARCH_MODE_TO -> {
                                // viewmodel에 도착지 저장
                                searchKeywordViewmodel.setArrival(keyword)
                                // 경로 검색 화면으로 되돌아가기
                                searchNavController.popBackStack()
                            }
                        }
                    },
                    onGoBack = {
                        when (searchMode) {
                            LOCATION_SEARCH_MODE -> {
                                rootNavController.navigate(NavRoute.MainFlow.route) {
                                    popUpTo(NavRoute.SearchFlow.route) { inclusive = true }
                                }
                            }
                            else -> {
                                searchNavController.popBackStack()
                            }
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
                    onSearchDirection = {
                        searchNavController.navigate(NavRoute.DirectionSearch.route)
                    },
                )
            }
            composable(route = NavRoute.DirectionSearch.route) { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    searchNavController.getBackStackEntry(SEARCH_GRAPH_ROUTE)
                }
                val searchKeywordViewmodel: SearchKeywordViewmodel = hiltViewModel(parentEntry)

                DirectionSearchScreen(
                    searchViewmodel = searchKeywordViewmodel,
                    onGoBack = {
                        searchNavController.popBackStack()
                        searchKeywordViewmodel.deleteDirectionData()
                    },
                    onDirect = {
                        searchNavController.navigate(NavRoute.Direction.route)
                    },
                    setDepart = {
                        searchNavController.navigate(NavRoute.Search.route + "/$DIRECTION_SEARCH_MODE_FROM")
                    },
                    setArrival = {
                        searchNavController.navigate(NavRoute.Search.route + "/$DIRECTION_SEARCH_MODE_TO")
                    }
                )
            }
            composable(route = NavRoute.Direction.route) { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    searchNavController.getBackStackEntry(SEARCH_GRAPH_ROUTE)
                }
                val searchKeywordViewmodel: SearchKeywordViewmodel = hiltViewModel(parentEntry)

                DirectionScreen(
                    searchViewmodel = searchKeywordViewmodel,
                    mapViewmodel = mapViewmodel,
                    onGoBack = {
                        searchNavController.popBackStack()
                        searchKeywordViewmodel.deleteDirectionData()
                    }
                )
            }
        }
    }
}
