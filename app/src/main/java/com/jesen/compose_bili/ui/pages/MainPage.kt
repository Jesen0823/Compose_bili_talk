package com.jesen.compose_bili.ui.pages

//import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.jesen.compose_bili.MainActivity
import com.jesen.compose_bili.navigation.BottomNav
import com.jesen.compose_bili.navigation.BottomNavHost
import com.jesen.compose_bili.navigation.BottomNavigationScreen

/**
 * 首屏 主页面
 * */
@ExperimentalMaterialApi
@ExperimentalFoundationApi
@ExperimentalPagerApi
@ExperimentalAnimationApi
@Composable
fun MainPage(activity: MainActivity) {
    val list = listOf(
        BottomNav.Screens.Home,
        BottomNav.Screens.Ranking,
        BottomNav.Screens.Favorite,
        BottomNav.Screens.Profile,
    )
    val navController = rememberNavController()

    Scaffold(bottomBar = {
        BottomNavigationScreen(
            bottomItem = BottomNav.bottomNavRoute.value,
            navController = navController,
            items = list
        )
    }) {
        BottomNavHost(navHostController = navController, activity)
    }
}