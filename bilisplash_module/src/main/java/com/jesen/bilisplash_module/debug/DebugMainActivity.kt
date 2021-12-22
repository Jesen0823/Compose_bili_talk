package com.jesen.bilisplash_module.debug

import android.content.res.Configuration
import android.os.Bundle
import android.view.Window
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.LocalOverScrollConfiguration
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.navigation.NavDeepLink
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.jesen.bilisplash_module.splash.SplashScreen
import com.jesen.bilisplash_module.splash.SplashViewModel
import com.jesen.bilisplash_module.ui.theme.Compose_bili_talkTheme
import com.jesen.common_util_lib.utils.LocalNavController
import com.jesen.common_util_lib.utils.LocalScreenOrientation
import com.jesen.common_util_lib.utils.oLog

val test = "* Compatibly class for the SplashScreen API introduced in API 31.\n" +
        " *\n" +
        " * On API 31+ (Android 12+) this class calls the platform methods.\n" +
        " *\n" +
        " * Prior API 31, the platform behavior is replicated with the exception of the Animated Vector\n" +
        " * Drawable support on the launch screen.\n" +
        " *\n" +
        " * To use this class, the theme of the starting Activity needs set [R.style.Theme_SplashScreen] as\n" +
        " * its parent and the [R.attr.windowSplashScreenAnimatedIcon] and [R.attr.postSplashScreenTheme]`\n" +
        " * attribute need to be set. * Sets a listener that will be called when the splashscreen is ready to be removed.\n" +
        "     *\n" +
        "     * If a listener is set, the splashscreen won't be automatically removed and the application\n" +
        "     * needs to manually call [SplashScreenViewProvider.remove].\n" +
        "     *\n" +
        "     * IF no listener is set, the splashscreen will be automatically removed once the app is\n" +
        "     * ready to draw.\n" +
        "     *\n" +
        "     * The listener will be called on the ui thread.\n" +
        "     *\n" +
        "     * @param listener The [OnExitAnimationListener] that will be called when the splash screen\n" +
        "     * is ready to be dismissed."

class DebugMainActivity : ComponentActivity() {

    private var screenOrientation by mutableStateOf(Configuration.ORIENTATION_PORTRAIT)

    val viewModel by viewModels<SplashViewModel>()

    private val TAG = "DebugMainActivity"

    @ExperimentalFoundationApi
    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 全屏
        WindowCompat.setDecorFitsSystemWindows(window, false)
        requestWindowFeature(Window.FEATURE_NO_TITLE)

        // 初始化启动页面
        installSplashScreen()

        setContent {
            val navController = rememberAnimatedNavController()
            CompositionLocalProvider(
                LocalScreenOrientation provides screenOrientation,
                LocalNavController provides navController,
                LocalOverScrollConfiguration provides null
            ) {
                // 处理状态栏
                ProvideWindowInsets {

                    Compose_bili_talkTheme {
                        val systemUiController = rememberSystemUiController()
                        val primaryColor = MaterialTheme.colors.background
                        val dark = MaterialTheme.colors.isLight

                        // 设置系统栏
                        SideEffect {
                            systemUiController.setNavigationBarColor(
                                primaryColor,
                                darkIcons = dark
                            )
                            systemUiController.setStatusBarColor(
                                Color.Transparent,
                                darkIcons = dark
                            )
                        }
                        Scaffold() {
                            /*Column(modifier = Modifier.fillMaxSize()) {
                                Spacer(
                                    modifier = Modifier
                                        .statusBarsHeight()
                                        .fillMaxWidth()
                                )*/

                            AnimatedNavHost(
                                modifier = Modifier.fillMaxSize(),
                                navController = navController,
                                startDestination = "splash",
                                enterTransition = {
                                    slideInHorizontally(
                                        initialOffsetX = {
                                            it
                                        },
                                        animationSpec = tween()
                                    )
                                },
                                exitTransition = {
                                    slideOutHorizontally(
                                        targetOffsetX = {
                                            -it
                                        },
                                        animationSpec = tween()
                                    ) + fadeOut(
                                        animationSpec = tween()
                                    )
                                },
                                popEnterTransition = {
                                    slideInHorizontally(
                                        initialOffsetX = {
                                            -it
                                        },
                                        animationSpec = tween()
                                    )
                                },
                                popExitTransition = {
                                    slideOutHorizontally(
                                        targetOffsetX = {
                                            it
                                        },
                                        animationSpec = tween()
                                    )
                                }
                            ) {
                                composable(
                                    route = "splash",
                                    exitTransition = {
                                        fadeOut()
                                    }
                                ) {
                                    SplashScreen(
                                        splashViewModel = viewModel,

                                        // 启动页下一步导航处理：
                                        navNexEvent = { isLogined ->
                                            oLog("$TAG splash isLogined:$isLogined")
                                            if (isLogined) {
                                                navController.navigate("main/推荐") {
                                                    popUpTo("splash") {
                                                        inclusive = true
                                                    }
                                                }
                                            } else {
                                                // 登录
                                                navController.navigate("login") {
                                                    popUpTo("splash") {
                                                        inclusive = true
                                                    }
                                                }
                                            }
                                        })
                                }

                                composable(
                                    route = "main/{name}",
                                    arguments = listOf(
                                        navArgument("name") {
                                            type = NavType.StringType
                                        }
                                    ),
                                    deepLinks = listOf(NavDeepLink("https://compose.bili.com/main/{name}"))
                                ) {
                                    DebugMainScreen(
                                        navController,
                                        it.arguments?.getString("name")!!
                                    )
                                }
                                composable(
                                    route = "common/profile?userName={userName}&age={age}",
                                    arguments = listOf(
                                        navArgument(
                                            name = "userName"
                                        ) {
                                            type = NavType.StringType
                                            defaultValue = ""
                                        },
                                        navArgument(
                                            name = "age"
                                        ) {
                                            type = NavType.IntType
                                            defaultValue = -1
                                        },
                                    ),
                                    deepLinks = listOf(NavDeepLink("https://compose.bili.com/common/profile?userName={userName}&age={age}"))
                                ) {
                                    DebugProfileScreen(
                                        navController = navController,
                                        userName = it.arguments?.getString("userName"),
                                        age = it.arguments?.getString("age")
                                    )
                                }


                                /*dialog("playlist?nid={nid}", arguments = listOf(
                                    navArgument("nid") {
                                        defaultValue = 0
                                        type = NavType.IntType
                                    }
                                )) {
                                    PlaylistDialog(
                                        navController,
                                        it.arguments!!.getInt("nid"),
                                        it.arguments!!.getString("playlist-id") ?: ""
                                    )
                                }*/

                                composable("login") {
                                    DebugLoginScreen(navController)
                                }
                            }

                            //}
                        }
                        /* BackHandler {
                             val curBackStackEntry = navController.currentBackStackEntry
                             val curDestination = navController.currentDestination
                             val preBackStackEntry = navController.previousBackStackEntry
                             val backQueue1 = navController.backQueue.first().destination.route
                             val backQueueLast = navController.backQueue.last().destination.route
                             oLog("MainActivity, backHandler,curBackStackEntry: ${curBackStackEntry?.destination?.route}")
                             oLog("MainActivity, backHandler,curDestination: ${curDestination?.route}")
                             oLog("MainActivity, backHandler,backQueue1: ${backQueue1}")
                             oLog("MainActivity, backHandler,backQueueLast: ${backQueueLast}")
                         }*/


                    }
                }
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (screenOrientation != newConfig.orientation) {
            screenOrientation = newConfig.orientation
            oLog("onConfigurationChanged: CONFIG CHANGE: ${newConfig.orientation}")
        }
    }
}


