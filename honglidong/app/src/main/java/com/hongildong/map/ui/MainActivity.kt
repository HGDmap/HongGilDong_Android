package com.hongildong.map.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.hongildong.map.navGraph.AppNavHost
import com.hongildong.map.navGraph.BottomNavigationBar
import com.hongildong.map.navGraph.MainNavHost
import com.hongildong.map.ui.theme.HongildongTheme
import com.hongildong.map.ui.util.map.MapBackground
import com.hongildong.map.ui.util.map.MapViewmodel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HongildongTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White
                ) {
                    AppNavHost()
                }
            }
        }
    }
}

@Composable
fun MainScreen(
    rootNavController: NavHostController
) {
    val navController = rememberNavController()
    val mapViewmodel: MapViewmodel = hiltViewModel()

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        MapBackground(
            viewModel = mapViewmodel
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = paddingValues.calculateBottomPadding())
                .background(Color.Transparent)
        ) {
            MainNavHost(
                rootNavController = rootNavController,
                mainNavController = navController,
                mapViewmodel = mapViewmodel
            )
        }
    }
}



