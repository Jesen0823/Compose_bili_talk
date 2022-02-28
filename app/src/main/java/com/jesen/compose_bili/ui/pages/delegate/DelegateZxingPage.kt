package com.jesen.compose_bili.ui.pages.delegate

import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.pager.ExperimentalPagerApi
import com.jesen.common_util_lib.utils.LocalNavController
import com.jesen.common_util_lib.utils.extUrlEncode
import com.jesen.common_util_lib.utils.showToast
import com.jesen.compose_bili.navigation.NavUtil
import com.jesen.compose_bili.navigation.PageRoute
import com.jesen.qrzxing.compose.ZxingComposable
import kotlinx.coroutines.launch

/**
 * 扫码页面
 */
@ExperimentalPagerApi
@ExperimentalMaterialApi
@ExperimentalFoundationApi
@ExperimentalAnimationApi
@Composable
fun DelegateZxingPage() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val navController = LocalNavController.current

    ZxingComposable { result ->
        Log.d("zx", "result:$result")
        if(result.isNullOrEmpty()){
            showToast(context = context, msg = "获取失败")
        }else if (result.startsWith("http://",true)
            || result.startsWith("https://",true)){

            scope.launch {
                NavUtil.doPageNavigationTo(
                    navController,
                    PageRoute.WEB_VIEW_ROUTE.replaceAfter(
                        "=",
                        result.extUrlEncode()?:""
                    ),
                    false,
                    popUpToRoute = PageRoute.MAIN_PAGE
                )
            }
        }else{
            showToast(context = context, msg = result)
        }
    }
}