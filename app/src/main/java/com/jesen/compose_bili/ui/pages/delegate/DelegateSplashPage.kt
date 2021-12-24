package com.jesen.compose_bili.ui.pages.delegate

import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import com.google.accompanist.pager.ExperimentalPagerApi
import com.jesen.bilisplash_module.splash.SplashScreen
import com.jesen.bilisplash_module.splash.SplashViewModel
import com.jesen.common_util_lib.utils.LocalMainActivity
import com.jesen.common_util_lib.utils.LocalNavController
import com.jesen.common_util_lib.utils.oLog
import com.jesen.compose_bili.navigation.NavUtil
import com.jesen.compose_bili.navigation.PageRoute
import com.jesen.compose_bili.utils.replaceRegex
import kotlinx.coroutines.launch

/**
 * 启动页
 */
@ExperimentalPagerApi
@ExperimentalAnimationApi
@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Composable
fun DelegateSplash() {

    val activity = LocalMainActivity.current
    val splashViewModel by activity.viewModels<SplashViewModel>()
    val navController = LocalNavController.current
    val coroutineScope = rememberCoroutineScope()

    SplashScreen(
        splashViewModel = splashViewModel,
        navNexEvent = { isLogined ->

            oLog("DelegateSplash, splash isLogined:$isLogined")
            coroutineScope.launch {
                if (isLogined) {
                    NavUtil.doPageNavigationTo(
                        navController,
                        replaceRegex(PageRoute.MAIN_PAGE, "0"),
                        false
                    )
                } else {
                    // 登录
                    NavUtil.doPageNavigationTo(navController, PageRoute.LOGIN_ROUTE, false)
                }
            }
        }
    )
}