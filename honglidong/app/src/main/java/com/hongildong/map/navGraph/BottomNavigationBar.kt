package com.hongildong.map.navGraph

import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.hongildong.map.R
import com.hongildong.map.ui.theme.AppTypography
import com.hongildong.map.ui.theme.Gray400
import com.hongildong.map.ui.theme.PrimaryMid
import com.hongildong.map.ui.theme.White


data class BottomNavItem(
    val label: String,
    val icon: Int,
    val route: String
)

object BottomNavItems {
    val NavItems = listOf(
        BottomNavItem(
            label = "즐겨찾기",
            icon = R.drawable.ic_bookmark,
            route = "bookmark"
        ),
        BottomNavItem(
            label = "주변",
            icon = R.drawable.ic_nearby,
            route = "nearby"
        ),
        BottomNavItem(
            label = "마이",
            icon = R.drawable.ic_profile,
            route = "profile"
        ),
    )
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    BottomAppBar(
        containerColor = White,
        modifier = Modifier.shadow(elevation = 8.dp)
    ) {
        val backStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = backStackEntry?.destination?.route

        BottomNavItems.NavItems.forEach { navItem ->
            NavigationBarItem(
                selected = currentRoute == navItem.route,
                onClick = {
                    navController.navigate(navItem.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(painterResource(navItem.icon), contentDescription = navItem.label)
                },
                label = {
                    Text(
                        text = navItem.label,
                        style = AppTypography.Bold_13
                            .copy(color = if (currentRoute == navItem.route) PrimaryMid else Gray400)
                    )
                },
                colors = NavigationBarItemColors(
                    selectedIconColor = PrimaryMid,
                    selectedTextColor = PrimaryMid,
                    unselectedIconColor = Gray400,
                    unselectedTextColor = Gray400,
                    selectedIndicatorColor = White,
                    disabledIconColor = Gray400,
                    disabledTextColor = Gray400,
                )
            )
        }

    }
}