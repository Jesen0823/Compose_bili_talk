package com.jesen.compose_bili.navigation

import android.os.Build
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.accompanist.pager.ExperimentalPagerApi
import com.jesen.compose_bili.R
import com.jesen.compose_bili.ui.pages.mainchildren.FavoritePage
import com.jesen.compose_bili.ui.pages.mainchildren.HomeTabPage
import com.jesen.compose_bili.ui.pages.mainchildren.ProfilePage
import com.jesen.compose_bili.ui.pages.mainchildren.RankingPage
import com.jesen.compose_bili.ui.theme.bili_90
import com.jesen.compose_bili.ui.theme.gray400

/**
 * 首页底部页面路由定义
 * */
sealed class Screens(val title: String, val route: String, @DrawableRes val icons: Int) {

    object Home : Screens(title = "首页", route = "home_route", icons = R.drawable.round_home_24)

    object Ranking :
        Screens(title = "排行", route = "ranking_route", icons = R.drawable.round_filter_24)

    object Favorite :
        Screens(title = "收藏", route = "fav_route", icons = R.drawable.round_favorite_24)

    object Profile :
        Screens(title = "我的", route = "profile_route", icons = R.drawable.round_person_24)
}

/**
 * 将Home设为默认页面
 * */
@RequiresApi(Build.VERSION_CODES.O)
@ExperimentalAnimationApi
@ExperimentalPagerApi
@Composable
fun BottomNavHost(navHostController: NavHostController) {
    NavHost(navController = navHostController, startDestination = Screens.Home.route) {
        composable(route = Screens.Home.route) {
            HomeTabPage()
        }
        composable(route = Screens.Ranking.route) {
            RankingPage()
        }
        composable(route = Screens.Favorite.route) {
            FavoritePage()
        }
        composable(route = Screens.Profile.route) {
            ProfilePage()
        }
    }
}


/**
 * 底部导航栏
 * */
@Composable
fun BottomNavigationScreen(navController: NavController, items: List<Screens>) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val destination = navBackStackEntry?.destination

    BottomNavigation(backgroundColor = Color.White, elevation = 12.dp) {
        items.forEach { screen ->
            BottomNavigationItem(
                selected = destination?.route == screen.route,
                onClick = {
                    navController.navigate(screen.route) {
                        launchSingleTop = true
                        popUpTo(navController.graph.findStartDestination().id) {
                            // 防止状态丢失
                            saveState = true
                        }
                        // 恢复Composable的状态
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        painter = painterResource(id = screen.icons),
                        contentDescription = null
                    )
                },
                label = { Text(screen.title) },
                alwaysShowLabel = true,
                unselectedContentColor = gray400,
                selectedContentColor = bili_90,
            )

        }
    }
}