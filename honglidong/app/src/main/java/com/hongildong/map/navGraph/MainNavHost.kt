package com.hongildong.map.navGraph

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.hongildong.map.data.entity.SearchableNodeType
import com.hongildong.map.ui.bookmark.BookmarkFolderInsideScreen
import com.hongildong.map.ui.bookmark.BookmarkViewModel
import com.hongildong.map.ui.home.BookmarkScreen
import com.hongildong.map.ui.home.FacilityType
import com.hongildong.map.ui.home.MainViewmodel
import com.hongildong.map.ui.home.NearbyScreen
import com.hongildong.map.ui.home.ProfileScreen
import com.hongildong.map.ui.home.SearchAllEventScreen
import com.hongildong.map.ui.home.SearchTypeFacilityScreen
import com.hongildong.map.ui.search.location_detail.facility.photo.ImageDetail
import com.hongildong.map.ui.search.location_detail.facility.review.ReviewScreen
import com.hongildong.map.ui.search.location_detail.facility.review.ReviewViewModel
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
                    mapViewModel = mapViewmodel,
                    bottomSheetViewModel = bottomSheetViewModel,
                    onSearchFacility = { facilityInfo ->
                        rootNavController.navigate(NavRoute.SearchFlow.createRoute(
                            type = facilityInfo.type ?: SearchableNodeType.FACILITY.apiName,
                            name = facilityInfo.name,
                            id = facilityInfo.id
                        ))
                    },
                    onClickTag = {
                        when (it) {
                            FacilityType.EVENT.apiName -> {
                                mainNavController.navigate(NavRoute.AllEvent.route)
                            }
                            else -> {
                                val type = FacilityType.fromApiName(it)?.displayName ?: "temp"
                                mainNavController.navigate(NavRoute.TypeFacility.route + "/${type}")
                            }
                        }
                    }
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
                    onClickFolder = { folderId ->
                        mainNavController.navigate(NavRoute.BookmarkFolderInside.route + "/$folderId")
                    },
                    bottomSheetViewModel = bottomSheetViewModel,
                    bookmarkViewModel = bookmarkViewModel,
                    onClickTag = {
                        when (it) {
                            FacilityType.EVENT.apiName -> {
                                mainNavController.navigate(NavRoute.AllEvent.route)
                            }
                            else -> {
                                val type = FacilityType.fromApiName(it)?.displayName ?: "temp"
                                mainNavController.navigate(NavRoute.TypeFacility.route + "/${type}")
                            }
                        }
                    }
                )
            }
            composable(
                route = NavRoute.BookmarkFolderInside.route + "/{folderId}",
                arguments = listOf(
                    navArgument("folderId") { type = NavType.IntType }
                )
            ) {
                val folderId = it.arguments?.getInt("folderId") ?: 0
                BookmarkFolderInsideScreen(
                    folderId = folderId,
                    bottomSheetViewModel = bottomSheetViewModel,
                    bookmarkViewModel = bookmarkViewModel,
                    onClickBookmark = { bookmarkInfo ->
                        rootNavController.navigate(NavRoute.SearchFlow.createRoute(
                            type = bookmarkInfo.type,
                            name = bookmarkInfo.name,
                            id = bookmarkInfo.id
                        ))
                    },
                    onGoBack = {
                        mainNavController.popBackStack()
                    }
                )
            }
            composable(route = NavRoute.Profile.route) { backStackEntry ->
                // MAIN_GRAPH_ROUTE를 찾아 ViewModel을 공유
                val parentEntry = remember(backStackEntry) {
                    mainNavController.getBackStackEntry(MAIN_GRAPH_ROUTE)
                }
                val reviewViewmodel = hiltViewModel<ReviewViewModel>(parentEntry)

                ProfileScreen(
                    bottomSheetViewModel = bottomSheetViewModel,
                    onDeleteReview = {
                        reviewViewmodel.deleteReview(it)
                    },
                    onUpdateReview = {
                        reviewViewmodel.setTargetReview(it)
                        mainNavController.navigate(NavRoute.Review.route + "/${it.facilityName}/${it.facilityId}")
                    },
                    onClickPhoto = {
                        mainNavController.navigate(NavRoute.ImageDetail.route + "?imageUrl=${Uri.encode(it)}")
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
                // MAIN_GRAPH_ROUTE를 찾아 ViewModel을 공유
                val parentEntry = remember(backStackEntry) {
                    mainNavController.getBackStackEntry(MAIN_GRAPH_ROUTE)
                }
                val reviewViewmodel = hiltViewModel<ReviewViewModel>(parentEntry)
                val facilityName = backStackEntry.arguments?.getString("facilityName") ?: ""
                val facilityId = backStackEntry.arguments?.getInt("facilityId") ?: 0

                ReviewScreen(
                    facilityName = facilityName,
                    reviewViewModel = reviewViewmodel,
                    reviewMode = 1,
                    onGoBack = {
                        reviewViewmodel.clearReviewInfo()
                        mainNavController.popBackStack()
                    },
                    onDone = { content: String, recommend: String, rating: String ->
                        reviewViewmodel.updateReview(
                            isNewReview = false,
                            facilityId = facilityId,
                            content = content,
                            recommend = recommend,
                            rating = rating
                        )
                    }
                )
            }
            // 이미지 상세보기 화면
            composable(
                route = NavRoute.ImageDetail.route + "?imageUrl={imageUrl}",
                arguments = listOf(
                    navArgument("imageUrl") { type = NavType.StringType },
                )
            ) { backStackEntry ->
                val imageUrl = backStackEntry.arguments?.getString("imageUrl") ?: ""
                ImageDetail(
                    url = imageUrl,
                    onGoBack = {
                        mainNavController.popBackStack()
                    }
                )
            }
            composable(
                route = NavRoute.AllEvent.route
            ) {
                val mainViewmodel = hiltViewModel<MainViewmodel>()

                SearchAllEventScreen(
                    mainViewmodel = mainViewmodel,
                    mapViewModel = mapViewmodel,
                    searchedWord = "이벤트",
                    onClickItem = { event ->
                        rootNavController.navigate(NavRoute.SearchFlow.createRoute(
                            type = SearchableNodeType.EVENT.apiName,
                            name = event.name,
                            id = event.id
                        ))
                    },
                    onGoBack = {
                        mainNavController.popBackStack()
                        mapViewmodel.clearMarker()
                    }
                )
            }
            composable(
                route = NavRoute.TypeFacility.route + "/{type}"
            ) { backStackEntry ->
                val mainViewmodel = hiltViewModel<MainViewmodel>()
                val type = backStackEntry.arguments?.getString("type") ?: ""

                SearchTypeFacilityScreen(
                    bottomSheetViewModel = bottomSheetViewModel,
                    bookmarkViewModel = bookmarkViewModel,
                    mainViewmodel = mainViewmodel,
                    mapViewModel = mapViewmodel,
                    searchedWord = type,
                    onClickItem = { facility ->
                        rootNavController.navigate(NavRoute.SearchFlow.createRoute(
                            type = facility.type ?: SearchableNodeType.FACILITY.apiName,
                            name = facility.name ?: "",
                            id = facility.id ?: 0
                        ))
                    },
                    onGoBack = {
                        mainNavController.popBackStack()
                        mapViewmodel.clearMarker()
                    }
                )
            }
        }
    }
}
