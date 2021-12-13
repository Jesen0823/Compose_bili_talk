package com.jesen.compose_bili.navigation

import androidx.annotation.DrawableRes
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import coil.annotation.ExperimentalCoilApi
import com.google.accompanist.pager.ExperimentalPagerApi
import com.jesen.compose_bili.BiliApp
import com.jesen.compose_bili.MainActivity
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

val context = BiliApp.mContext

object BottomNav {
    sealed class Screens(val title: String, val route: String, @DrawableRes val icons: Int) {


        object Home : Screens(
            title = context.getString(R.string.main_bottom_home),
            route = "home_route",
            icons = R.drawable.round_home_24
        )

        object Ranking :
            Screens(
                title = context.getString(R.string.main_bottom_rank),
                route = "ranking_route",
                icons = R.drawable.round_filter_24
            )

        object Favorite :
            Screens(
                title = context.getString(R.string.main_bottom_fav),
                route = "fav_route",
                icons = R.drawable.round_favorite_24
            )

        object Profile :
            Screens(
                title = context.getString(R.string.main_bottom_profile),
                route = "profile_route",
                icons = R.drawable.round_person_24
            )
    }

    //记录BottomNav当前的Item
    val bottomNavRoute = mutableStateOf<Screens>(Screens.Home)

}

/**
 * 将Home设为默认页面
 * */
@ExperimentalCoilApi
@ExperimentalFoundationApi
@ExperimentalMaterialApi
@ExperimentalAnimationApi
@ExperimentalPagerApi
@Composable
fun BottomNavHost(navHostController: NavHostController, activity: MainActivity) {
    NavHost(navController = navHostController, startDestination = BottomNav.Screens.Home.route) {
        composable(route = BottomNav.Screens.Home.route) {
            HomeTabPage(activity)
        }
        composable(route = BottomNav.Screens.Ranking.route) {
            RankingPage(activity)
        }
        composable(route = BottomNav.Screens.Favorite.route) {
            FavoritePage(activity)
        }
        composable(route = BottomNav.Screens.Profile.route) {
            ProfilePage()
        }
    }
}


/**
 * 底部导航栏
 * */
@Composable
fun BottomNavigationScreen(
    navController: NavController,
    items: List<BottomNav.Screens>,
    bottomItem: BottomNav.Screens
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val destination = navBackStackEntry?.destination

    BottomNavigation(backgroundColor = Color.White, elevation = 12.dp) {
        items.forEach { screen: BottomNav.Screens ->

            //记录动画
            val scaleValue = remember { Animatable(1F) }
            //开启线程执行动画
            LaunchedEffect(bottomItem) {
                if (bottomItem == screen)
                    scaleValue.animateTo(1.2f)
                else scaleValue.animateTo(1F)
            }

            BottomNavigationItem(
                modifier = Modifier
                    .wrapContentSize()
                    .scale(scaleValue.value),
                selected = destination?.route == screen.route,
                icon = {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(id = screen.icons),
                        contentDescription = null
                    )
                },
                onClick = {
                    // 同一个bottomItem不再重复触发
                    if (screen == bottomItem) {
                        return@BottomNavigationItem
                    }
                    // 更新当前底部选项
                    BottomNav.bottomNavRoute.value = screen

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
                label = { Text(screen.title) },
                alwaysShowLabel = true,
                unselectedContentColor = gray400,
                selectedContentColor = bili_90,
            )

        }
    }
}