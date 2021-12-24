package com.jesen.compose_bili.navigation

//import androidx.navigation.compose.NavHost
//import androidx.navigation.compose.composable
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.navigation.NavDeepLink
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.pager.ExperimentalPagerApi
import com.jesen.common_util_lib.utils.LocalNavController
import com.jesen.common_util_lib.utils.extUrlEncode
import com.jesen.compose_bili.ui.pages.MainPage
import com.jesen.compose_bili.ui.pages.NoticeListPage
import com.jesen.compose_bili.ui.pages.SearchPage
import com.jesen.compose_bili.ui.pages.VideoDetailPage
import com.jesen.compose_bili.ui.pages.delegate.DelegateSplash
import com.jesen.compose_bili.ui.pages.delegate.DelegateWebView
import com.jesen.compose_bili.ui.pages.user.LoginPage
import com.jesen.compose_bili.ui.pages.user.RegisterPage

/**
 * 定义普通页面路由
 * */
object PageRoute {
    const val SPLASH_ROUTE = "splash"
    const val LOGIN_ROUTE = "login"
    const val REGISTER_ROUTE = "register"
    const val VIDEO_DETAIL_ROUTE = "detail/videoDetail?videoM={videoM}"
    const val MAIN_PAGE = "main/{bottomIndex}"
    const val NOTICE_ROUTE = "notice_list"
    const val SEARCH_ROUTE = "search"
    const val WEB_VIEW_ROUTE = "web_view/web?link={linkUrl}"
}

/**
 * 将页面与路由关联
 * */
@ExperimentalComposeUiApi
@ExperimentalFoundationApi
@ExperimentalMaterialApi
@ExperimentalAnimationApi
@ExperimentalPagerApi
@Composable
fun PageNavHost() {
    val navHostController = LocalNavController.current

    /*NavHost(navController = navHostController, startDestination = PageRoute.SPLASH_ROUTE) {
        composable(route = PageRoute.SPLASH_ROUTE) {
            DelegateSplash()
        }
        composable(route = PageRoute.LOGIN_ROUTE) {
            LoginPage(activity = mainActivity)
        }
        composable(route = PageRoute.REGISTER_ROUTE) {
            RegisterPage(activity = mainActivity)
        }
        composable(
            // 定义参数类型
            route = PageRoute.VIDEO_DETAIL_ROUTE,
            arguments = listOf(navArgument("videoId") { type = NavType.StringType }),

            ) { backStackEntry ->
            // 接收参数
            val param = backStackEntry.arguments?.getString("videoId")
            oLog("跳转后： ${navHostController.previousBackStackEntry?.destination}")
            VideoDetailPage(param)
        }
        composable(route = PageRoute.MAIN_PAGE) {
            MainPage(activity = mainActivity)
        }
        composable(route = PageRoute.NOTICE_ROUTE) {
            NoticeListPage(activity = mainActivity)
        }
        composable(route = PageRoute.SEARCH_ROUTE) {
            SearchPage(activity = mainActivity)
        }
        composable(route = PageRoute.WEB_VIEW_ROUTE) {
            DelegateWebView(url = "https://www.baidu.com")
        }
    }*/

    // 可定义动画的导航库的使用 https://google.github.io/accompanist/navigation-animation/
    AnimatedNavHost(
        modifier = Modifier.fillMaxSize(),
        navController = navHostController,
        startDestination = PageRoute.SPLASH_ROUTE,
        enterTransition = {
            slideInHorizontally(
                initialOffsetX = { it },
                animationSpec = tween()
            )
        },
        exitTransition = {
            slideOutHorizontally(
                targetOffsetX = { -it },
                animationSpec = tween()
            ) + fadeOut(
                animationSpec = tween()
            )
        },
        popEnterTransition = {
            slideInHorizontally(
                initialOffsetX = { -it },
                animationSpec = tween()
            )
        },
        popExitTransition = {
            slideOutHorizontally(
                targetOffsetX = { it },
                animationSpec = tween()
            )
        }
    ) {
        composable(
            route = PageRoute.SPLASH_ROUTE,
            exitTransition = { fadeOut() }
        ) {
            DelegateSplash()
        }
        composable(route = PageRoute.LOGIN_ROUTE) {
            LoginPage()
        }
        composable(route = PageRoute.REGISTER_ROUTE) {
            RegisterPage()
        }
        composable(
            route = PageRoute.MAIN_PAGE,
            arguments = listOf(
                navArgument("bottomIndex") {
                    type = NavType.IntType
                    defaultValue = 0
                }
            ),
            deepLinks = listOf(NavDeepLink("https://compose.bili.com/main/{bottomIndex}"))
        ) {
            MainPage(bottomIndex = it.arguments?.getInt("bottomIndex") ?: 0)
        }
        composable(
            // 定义参数类型
            route = PageRoute.VIDEO_DETAIL_ROUTE,
            arguments = listOf(
                navArgument(name = "videoM") {
                    type = NavType.StringType
                    defaultValue = ""
                }
            ),
            deepLinks = listOf(
                NavDeepLink("https://compose.bili.com/detail/videoDetail?videoM={videoM}")
            )
        ) { backStackEntry ->
            // 接收参数
            val videoM = backStackEntry.arguments?.getString("videoM")
            VideoDetailPage(videoJson = videoM)
        }
        composable(route = PageRoute.NOTICE_ROUTE) {
            NoticeListPage()
        }
        composable(route = PageRoute.SEARCH_ROUTE) {
            SearchPage()
        }
        composable(
            route = PageRoute.WEB_VIEW_ROUTE,
            arguments = listOf(
                navArgument(name = "linkUrl") {
                    type = NavType.StringType
                    defaultValue = "https:www.baidu.com".extUrlEncode()
                }
            ),
            deepLinks = listOf(
                NavDeepLink("https://compose.bili.com/web_view/web?link={linkUrl}")
            )
        ) { backStackEntry ->
            val link = backStackEntry.arguments?.getString("linkUrl")
            DelegateWebView(url = link!!)
        }
    }
}
