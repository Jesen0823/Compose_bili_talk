package com.jesen.compose_bili.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraph.Companion.findStartDestination
//import androidx.navigation.compose.NavHost
//import androidx.navigation.compose.composable
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.pager.ExperimentalPagerApi
import com.jesen.compose_bili.MainActivity
import com.jesen.compose_bili.navigation.PageRoute.LOGIN_ROUTE
import com.jesen.compose_bili.navigation.PageRoute.REGISTER_ROUTE
import com.jesen.compose_bili.ui.pages.MainPage
import com.jesen.compose_bili.ui.pages.VideoDetailPage
import com.jesen.compose_bili.ui.pages.user.LoginPage
import com.jesen.compose_bili.ui.pages.user.RegisterPage

/**
 * 定义普通页面路由
 * */
object PageRoute {
    const val LOGIN_ROUTE = "login_route"
    const val REGISTER_ROUTE = "register_route"
    const val VIDEO_DETAIL_ROUTE = "video_detail_route"
    const val MAIN_PAGE = "main_route"
}

/**
 * 将页面与路由关联
 * */
@ExperimentalAnimationApi
@ExperimentalPagerApi
@Composable
fun PageNavHost(mainActivity: MainActivity) {
    val navHostController = MainActivity.pageNavController!!
    val isLogined = false // 是否登录

    // 初始页面
    val initRoute = if (isLogined) PageRoute.MAIN_PAGE else PageRoute.LOGIN_ROUTE
    /*NavHost(navController = navHostController, startDestination = initRoute) {
        composable(route = PageRoute.LOGIN_ROUTE) {
            LoginPage(activity = mainActivity)
        }
        composable(route = PageRoute.REGISTER_ROUTE) {
            RegisterPage(activity = mainActivity)
        }
        composable(route = PageRoute.VIDEO_DETAIL_ROUTE) {
            VideoDetailPage()
        }
        composable(route = PageRoute.MAIN_PAGE) {
            MainPage()
        }
    }*/

    // 可定义动画的导航库 https://google.github.io/accompanist/navigation-animation/
    AnimatedNavHost(navController = navHostController, startDestination = initRoute) {
        composable(
            route = PageRoute.LOGIN_ROUTE,
            enterTransition = {
                when (initialState.destination.route) {
                    REGISTER_ROUTE ->
                        expandHorizontally()
                        //slideInHorizontally(initialOffsetX = { 1000 }, animationSpec = tween(1000))
                        //slideIntoContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(1200))
                    else -> null
                }
            },
            exitTransition = {
                when (targetState.destination.route) {
                    REGISTER_ROUTE ->
                        slideOutHorizontally(targetOffsetX = { -0 }, animationSpec = tween(1000))
                       // slideOutOfContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(1200))
                    else -> null
                }
            },
            popEnterTransition = {
                when (initialState.destination.route) {
                    REGISTER_ROUTE ->
                        slideInHorizontally(initialOffsetX = { -0 }, animationSpec = tween(1000))
                        //slideIntoContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(1200))
                    else -> null
                }
            },
            popExitTransition = {
                when (targetState.destination.route) {
                    REGISTER_ROUTE ->
                        slideOutHorizontally(targetOffsetX = { 0 }, animationSpec = tween(1000))
                        //slideOutOfContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(1200))
                    else -> null
                }
            }
        ) {
            LoginPage(activity = mainActivity)
        }
        composable(
            route = PageRoute.REGISTER_ROUTE,
            enterTransition = {
                when (initialState.destination.route) {
                    REGISTER_ROUTE ->
                        slideIntoContainer(
                            AnimatedContentScope.SlideDirection.Up, animationSpec = tween(1200)
                        )
                    else -> null
                }
            },
            exitTransition = {
                when (targetState.destination.route) {
                    REGISTER_ROUTE ->
                        slideOutOfContainer(
                            AnimatedContentScope.SlideDirection.Up, animationSpec = tween(1200)
                        )
                    else -> null
                }
            },
            popEnterTransition = {
                when (initialState.destination.route) {
                    REGISTER_ROUTE ->
                        slideIntoContainer(
                            AnimatedContentScope.SlideDirection.Down, animationSpec = tween(1200)
                        )
                    else -> null
                }
            },
            popExitTransition = {
                when (targetState.destination.route) {
                    REGISTER_ROUTE ->
                        slideOutOfContainer(
                            AnimatedContentScope.SlideDirection.Down, animationSpec = tween(1200)
                        )
                    else -> null
                }
            }
            ) {
            RegisterPage(activity = mainActivity)
        }
        composable(route = PageRoute.VIDEO_DETAIL_ROUTE) {
            VideoDetailPage()
        }
        composable(route = PageRoute.MAIN_PAGE) {
            MainPage()
        }
    }

}

/**
 * 页面跳转
 * */
fun doPageNavigationTo(route: String) {
    val navController = MainActivity.pageNavController!!
    navController.navigate(route) {
        launchSingleTop = false

        popUpTo(navController.graph.findStartDestination().id) {
            // 防止状态丢失
            saveState = true
        }
        // 恢复Composable的状态
        restoreState = true
    }
}

/**
 * 页面回退
 * */
fun doPageNavBack(route: String?) {
    val navController = MainActivity.pageNavController!!
    route?.let {
        navController.popBackStack(route = it, inclusive = false)
    } ?: navController.popBackStack()
}