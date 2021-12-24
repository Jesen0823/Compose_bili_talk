package com.jesen.compose_bili.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.google.accompanist.pager.ExperimentalPagerApi

object NavUtil {

    /**
     * 页面跳转
     * */
    @ExperimentalFoundationApi
    @ExperimentalMaterialApi
    @ExperimentalAnimationApi
    @ExperimentalPagerApi
    fun doPageNavigationTo(navController:NavHostController,route: String,allowBack:Boolean=true) {
        navController.navigate(route) {
            launchSingleTop = false

            popUpTo(navController.graph.findStartDestination().id) {
                if (allowBack) {
                    // 防止状态丢失
                    saveState = true
                }else{
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
    fun doPageNavBack(navController:NavHostController,route: String? = null) {
        route?.let {
            navController.popBackStack(route = it, inclusive = false)
        } ?: navController.popBackStack()
    }
}