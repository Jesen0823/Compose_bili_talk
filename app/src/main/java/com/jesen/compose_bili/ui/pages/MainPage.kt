package com.jesen.compose_bili.ui.pages

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
@ExperimentalPagerApi
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