package com.jesen.compose_bili.ui.pages

//import androidx.compose.animation.ExperimentalAnimationApi
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.jesen.compose_bili.navigation.BottomNavHost
import com.jesen.compose_bili.navigation.BottomNavigationScreen
import com.jesen.compose_bili.navigation.Screens

/**
 * 首屏 主页面
 * */
@RequiresApi(Build.VERSION_CODES.O)
@ExperimentalPagerApi
@ExperimentalAnimationApi
@Composable
fun MainPage() {
    val list = listOf(
        Screens.Home,
        Screens.Ranking,
        Screens.Favorite,
        Screens.Profile,
    )
    val navController = rememberNavController()

    Scaffold(bottomBar = {
        BottomNavigationScreen(navController = navController, items = list)
    }) {
        BottomNavHost(navHostController = navController)
    }
}