package com.hongildong.map.navGraph

import android.net.Uri


sealed class NavRoute(val route: String) {
    // flow
    object EnterFlow: NavRoute("enter_flow")
    object MainFlow: NavRoute("main_flow")
    object SearchFlow: NavRoute("search_flow?type={type}&name={name}&id={id}") {
        // 일반 검색 루틴 -> 검색 화면으로 이동
        fun route() = "search_flow"

        // 이벤트/건물/시설 상세 화면으로 이동
        // type: 시설/이벤트/건물인지
        // name: 검색어
        // id: 시설/이벤트/건물 id
        fun createRoute(type: String, name: String, id: Int) = "search_flow?type=$type&name=${Uri.encode(name)}&id=$id"
    }

    // home
    object Nearby : NavRoute("nearby")
    object Bookmark : NavRoute("bookmark")
    object Profile : NavRoute("profile")
    object Main: NavRoute("main")

    // 북마크 폴더
    object BookmarkFolderInside: NavRoute("bookmark_folder_inside")

    // user
    object Enter: NavRoute("enter")
    object Login: NavRoute("login")
    object TermsAgreement: NavRoute("terms_agreement")
    object TermsDetail: NavRoute("terms_detail")
    object EmailEnter: NavRoute("email_enter")
    object PasswordEnter: NavRoute("password_enter")
    object UserInfoEnter: NavRoute("user_info_enter")

    // search
    object SearchRoot: NavRoute("search_root")
    object Search : NavRoute("search")
    object RawSearch: NavRoute("raw_search")
    object LocationDetail: NavRoute("location_detail")
    object BuildingDetail: NavRoute("building_detail")
    object FacilityDetail: NavRoute("facility_detail")
    object Review: NavRoute("review")

    // direct
    object DirectionSearch: NavRoute("direction_search")
    object Direction: NavRoute("direction")
}
