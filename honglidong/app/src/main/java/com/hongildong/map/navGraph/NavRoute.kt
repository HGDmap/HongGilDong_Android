package com.hongildong.map.navGraph


sealed class NavRoute(val route: String) {
    // flow
    object EnterFlow: NavRoute("enter_flow")
    object MainFlow: NavRoute("main_flow")
    object SearchFlow: NavRoute("search_flow")

    // home
    object Nearby : NavRoute("nearby")
    object Bookmark : NavRoute("bookmark")
    object Profile : NavRoute("profile")
    object Main: NavRoute("main")

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
    object FacilityDetail: NavRoute("facility_detail")
    object Review: NavRoute("review")

    // direct
    object DirectionSearch: NavRoute("direction_search")
    object Direction: NavRoute("direction")
}
