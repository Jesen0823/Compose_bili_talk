package com.jesen.compose_bili.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.jesen.common_util_lib.utils.oLog

object NavUtil {

    /**
     * 页面跳转
     * */
    @ExperimentalFoundationApi
    @ExperimentalMaterialApi
    @ExperimentalAnimationApi
    @ExperimentalPagerApi
    fun doPageNavigationTo(
        navController: NavHostController,
        route: String,
        allowBack: Boolean = true,
        popUpToRoute: String? = null, // 指定回退目标
        singTop:Boolean = true
    ) {
        oLog("naviagte page route: $route")
        navController.navigate(route) {
            launchSingleTop = singTop
            popUpToRoute?.let {
                popUpTo(it)
            } ?: popUpTo(navController.graph.findStartDestination().id) {
                if (allowBack) {
                    // 防止状态丢失
                    saveState = true
                } else {
                    // 清除导航后目的页之前的所有页面
                    inclusive = true
                }
            }
            // 恢复Composable的状态
            restoreState = true
        }
    }

    /**
     * 页面回退
     * */
    @ExperimentalFoundationApi
    @ExperimentalMaterialApi
    @ExperimentalAnimationApi
    @ExperimentalPagerApi
    fun doPageNavBack(navController: NavHostController, route: String? = null) {
        route?.let {
            navController.popBackStack(route = it, inclusive = false)
        } ?: navController.popBackStack()
    }
}