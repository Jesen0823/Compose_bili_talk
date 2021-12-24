package com.jesen.compose_bili.ui.pages

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.jesen.compose_bili.navigation.BottomNav
import com.jesen.compose_bili.navigation.BottomNavHost
import com.jesen.compose_bili.navigation.BottomNavigationScreen

/**
 * 首屏 主页面
 * */
@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@ExperimentalFoundationApi
@ExperimentalPagerApi
@ExperimentalAnimationApi
@Composable
fun MainPage(bottomIndex: Int = 0) {
    val list = listOf(
        BottomNav.Screens.Home,
        BottomNav.Screens.Ranking,
        BottomNav.Screens.Favorite,
        BottomNav.Screens.Profile,
    )
    val mainNavController = rememberNavController()
    val initBottomRoute = list[bottomIndex]

    Scaffold(bottomBar = {
        BottomNavigationScreen(
            bottomItem = BottomNav.bottomNavRoute.value,
            mainNavController = mainNavController,
            items = list
        )
    }) {
        BottomNavHost(mainNavHostController = mainNavController,initBottomRoute=initBottomRoute)
    }
}