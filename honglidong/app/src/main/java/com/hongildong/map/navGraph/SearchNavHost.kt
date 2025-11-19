package com.hongildong.map.navGraph

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.hongildong.map.data.entity.SearchKeyword
import com.hongildong.map.data.entity.SearchableNodeType
import com.hongildong.map.ui.bookmark.BookmarkViewModel
import com.hongildong.map.ui.search.SearchKeywordViewmodel
import com.hongildong.map.ui.search.SearchScreen
import com.hongildong.map.ui.search.SearchedFacilityListScreen
import com.hongildong.map.ui.search.direction.DirectionScreen
import com.hongildong.map.ui.search.direction.DirectionSearchScreen
import com.hongildong.map.ui.search.location_detail.facility.FacilityDetailScreen
import com.hongildong.map.ui.search.location_detail.LocationDetailScreen
import com.hongildong.map.ui.search.location_detail.facility.ReviewScreen
import com.hongildong.map.ui.search.location_detail.facility.ReviewViewModel
import com.hongildong.map.ui.util.bottomsheet.BottomSheetViewModel
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
    mapViewmodel: MapViewmodel,
    bookmarkViewModel: BookmarkViewModel,
    bottomSheetViewModel: BottomSheetViewModel
) {
    NavHost(
        navController = searchNavController,
        startDestination = SEARCH_GRAPH_ROUTE
    ) {
        navigation(
            route = SEARCH_GRAPH_ROUTE,
            startDestination = NavRoute.Search.route + "/$LOCATION_SEARCH_MODE"
        ) {
            // 검색 화면 - 검색 기록, 자동 완성 모두 여기임
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
                                if (keyword.nodeCode == SearchableNodeType.FACILITY.apiName) {
                                    searchNavController.navigate(NavRoute.FacilityDetail.route + "/${keyword.nodeName}")
                                } else {
                                    searchNavController.navigate(NavRoute.LocationDetail.route + "/${keyword.nodeName}")
                                }
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
                    onRawSearch = { query ->
                        when (searchMode) {
                            LOCATION_SEARCH_MODE -> {
                                searchNavController.navigate(NavRoute.RawSearch.route + "/${query}")
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
                    viewModel = searchKeywordViewmodel,
                    searchMode = searchMode
                )
            }
            // 일반 검색 - 검색버튼으로 검색시 제공할 시설 리스트 화면
            composable(route = NavRoute.RawSearch.route + "/{searchedWord}") { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    searchNavController.getBackStackEntry(SEARCH_GRAPH_ROUTE)
                }
                val searchKeywordViewmodel: SearchKeywordViewmodel = hiltViewModel(parentEntry)
                val searchedWord = backStackEntry.arguments?.getString("searchedWord") ?: ""

                SearchedFacilityListScreen(
                    searchViewmodel = searchKeywordViewmodel,
                    searchedWord = searchedWord,
                    onDirectItem = {
                        val target = SearchKeyword(
                            nodeName = it.name ?: it.nodeName ?: "temp",
                            id = it.id ?: 0,
                            nodeId = it.nodeId,
                            nodeCode = it.nodeCode ?: "",
                        )
                        // 길찾기 버튼 클릭시 해당 장소를 도착지로 설정
                        searchKeywordViewmodel.setArrival(target)
                        // 길찾기 화면으로 이동
                        searchNavController.navigate(NavRoute.DirectionSearch.route)
                    },
                    onClickItem = {
                        val target = SearchKeyword(
                            nodeName = it.name ?: it.nodeName ?: "temp",
                            id = it.id ?: 0,
                            nodeId = it.nodeId,
                            nodeCode = it.nodeCode ?: "",
                        )
                        // 아이템 클릭시 해당 장소 상세 정보 검색
                        searchKeywordViewmodel.onSearch(target)
                        // 장소 상세 정보 화면으로 이동
                        if (target.nodeCode == SearchableNodeType.FACILITY.apiName) {
                            searchNavController.navigate(NavRoute.FacilityDetail.route + "/${target.nodeName}")
                        } else {
                            searchNavController.navigate(NavRoute.LocationDetail.route + "/${target.nodeName}")
                        }
                    },
                    onGoBack = {
                        searchNavController.popBackStack()
                        mapViewmodel.clearMarker()
                    },
                    bottomSheetViewModel = bottomSheetViewModel,
                    bookmarkViewModel = bookmarkViewModel
                )

            }
            // 건물 상세 정보 화면으로 재활용 예정
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
                    bookmarkViewModel = bookmarkViewModel,
                    bottomSheetViewModel = bottomSheetViewModel,
                    onGoBack = {
                        searchNavController.popBackStack()
                    },
                    onSearchDirection = {
                        searchNavController.navigate(NavRoute.DirectionSearch.route)
                    },
                )
            }
            // 시설 상세 정보 화면
            composable(route = NavRoute.FacilityDetail.route + "/{searchedWord}") { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    searchNavController.getBackStackEntry(SEARCH_GRAPH_ROUTE)
                }
                val searchKeywordViewmodel: SearchKeywordViewmodel = hiltViewModel(parentEntry)
                val searchedWord = backStackEntry.arguments?.getString("searchedWord") ?: ""

                FacilityDetailScreen(
                    searchedWord = searchedWord,
                    searchViewmodel = searchKeywordViewmodel,
                    mapViewmodel = mapViewmodel,
                    bookmarkViewModel = bookmarkViewModel,
                    bottomSheetViewModel = bottomSheetViewModel,
                    onGoBack = {
                        searchKeywordViewmodel.eraseFacilityData()
                        searchNavController.popBackStack()
                    },
                    onSearchDirection = {
                        searchNavController.navigate(NavRoute.DirectionSearch.route)
                    },
                    onReview = {
                        searchNavController.navigate(NavRoute.Review.route + "/${it.name}/${it.id}")
                    }
                )
            }
            // 리뷰 작성 화면
            composable(
                route = NavRoute.Review.route + "/{facilityName}/{facilityId}",
                arguments = listOf(
                    navArgument("facilityName") { type = NavType.StringType },
                    navArgument("facilityId") { type = NavType.IntType }
                )
            ) { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    searchNavController.getBackStackEntry(SEARCH_GRAPH_ROUTE)
                }
                val reviewViewModel: ReviewViewModel = hiltViewModel()
                val facilityName = backStackEntry.arguments?.getString("facilityName") ?: ""
                val facilityId = backStackEntry.arguments?.getInt("facilityId") ?: 0

                ReviewScreen(
                    reviewViewModel = reviewViewModel,
                    facilityName = facilityName,
                    onGoBack = {
                        searchNavController.popBackStack()
                    },
                    onDone = {
                        reviewViewModel.createReview(facilityId, it)
                    }
                )
            }
            // 경로 검색 (시작점, 종료 지점)
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
            // 경로 화면
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
