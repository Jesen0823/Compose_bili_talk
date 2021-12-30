package com.jesen.compose_bili.ui.pages.mainchildren

import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.google.accompanist.pager.ExperimentalPagerApi
import com.jesen.bilibanner.BannerConfig
import com.jesen.common_util_lib.custonnested.NestedWrapCustomLayout
import com.jesen.common_util_lib.utils.LocalMainActivity
import com.jesen.common_util_lib.utils.LocalNavController
import com.jesen.common_util_lib.utils.advancedShadow
import com.jesen.compose_bili.navigation.NavUtil
import com.jesen.compose_bili.navigation.PageRoute
import com.jesen.compose_bili.ui.theme.bili_50
import com.jesen.compose_bili.ui.theme.gray200
import com.jesen.compose_bili.ui.widget.*
import com.jesen.compose_bili.viewmodel.ProfileViewModel
import com.jesen.retrofit_lib.model.DataProfile
import com.jesen.retrofit_lib.response.DataState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
//import kotlinx.coroutines.launch
import soup.compose.material.motion.MaterialFadeThrough


/**
 * 个人中心
 */
@ExperimentalComposeUiApi
@ExperimentalFoundationApi
@ExperimentalMaterialApi
@ExperimentalAnimationApi
@ExperimentalPagerApi
@Composable
fun ProfilePage() {
    val activity = LocalMainActivity.current
    val viewModel by activity.viewModels<ProfileViewModel>()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(key1 = viewModel.profileDataState) {
        viewModel.loadProfileInfo
        activity.lifecycleScope.launch {
            viewModel.loadProfileInfo
        }
    }

    val columnLazyState = rememberLazyListState()

    Box(modifier = Modifier.fillMaxSize()) {
        // 观察流数据状态更新UI
        MaterialFadeThrough(
            targetState = viewModel.profileDataState.collectAsState()
        ) { dataStoreData ->
            when (dataStoreData.value) {
                is DataState.Empty,
                is DataState.Loading -> {
                    ProfileLoading()
                }
                is DataState.Success -> {
                    // 展示详情内容
                    ProfileContentScreen(
                        coroutineScope,
                        viewModel,
                        columnLazyState,
                        dataStoreData.value.read().data
                    )
                }
                is DataState.Error -> {
                    SinglePageError()
                }
            }
        }
    }
}


/**
 * 内容展示
 */
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@ExperimentalMaterialApi
@ExperimentalPagerApi
@ExperimentalFoundationApi
@Composable
fun ProfileContentScreen(
    coroutineScope: CoroutineScope,
    viewModel: ProfileViewModel,
    columnLazyState: LazyListState,
    profileData: DataProfile
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
    ) {
        var isShowDialog by remember { mutableStateOf(false) }

        val navController = LocalNavController.current
        NestedWrapCustomLayout(
            columnTop = 202.dp,
            navigationIconSize = 80.dp,
            toolBarHeight = 56.dp,
            scrollableAppBarHeight = 202.dp,
            columnState = columnLazyState,
            scrollableAppBarBgColor = Color.LightGray,
            toolBar = { ProfileToolBar(profileData = profileData) },
            navigationIcon = { UserAdvertImg(advert = profileData.face) }, //默认为返回图标
            extendUsrInfo = { UserNameUI(profileData = profileData) },
            headerTop = { HeaderTop() },
            backSlideProgress = { progress ->

            }
        ) {

            /** 1. 广告banner */
            item {
                BiliBanner(
                    modifier = Modifier
                        .fillMaxWidth()
                        .advancedShadow(
                            color = gray200,
                            alpha = 0.8f,
                            shadowBlurRadius = 10.dp,
                            offsetX = 2.dp,
                            offsetY = 3.dp
                        ),
                    items = viewModel.bannerDataList,
                    config = BannerConfig(
                        indicatorColor = Color.White.copy(0.8f),
                        selectedColor = bili_50.copy(0.8f),
                        intervalTime = 3000
                    ),

                    itemOnClick = { banner ->
                        // 点击banner
                        coroutineScope.launch {
                            NavUtil.doPageNavigationTo(
                                navController,
                                PageRoute.WEB_VIEW_ROUTE.replaceAfter("=", banner.url)
                            )
                        }
                    }
                )
            }

            /** 2. 广告课程 */
            stickyHeader {
                ColumnStickHeader(title = "你的广告", subTitle = "这些都是flutter课程广告")
            }
            profileData.courseList?.let {
                item {
                    CourseListView(courseList = it)
                }
            }

            /** 3. 增值服务 */
            stickyHeader {
                ColumnStickHeader(title = "增值服务", subTitle = "点击子项可以跳转web")
            }
            profileData.benefitList?.let { nenefitList ->
                item {
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceAround,
                        state = rememberLazyListState(),
                        contentPadding = PaddingValues(vertical = 8.dp, horizontal = 2.dp),
                        content = {
                            items(count = nenefitList.size) { index ->
                                BenefitItemView(
                                    nenefitList[index],
                                    onclick = {
                                        coroutineScope.launch {
                                            NavUtil.doPageNavigationTo(
                                                navController,
                                                PageRoute.WEB_VIEW_ROUTE.replaceAfter(
                                                    "=",
                                                    nenefitList[index].url
                                                )
                                            )
                                        }
                                    }
                                )
                            }
                        }
                    )

                }
            }
            /** 4. 设置 */
            stickyHeader {
                ColumnStickHeader(title = "设置", subTitle = "")
            }
            item {
                ColumnSetting(
                    onLogoutClick = {
                        isShowDialog = true
                    },
                    onSwitchChange = {

                    },
                    onQRClick = {
                        coroutineScope.launch {
                            NavUtil.doPageNavigationTo(navController, PageRoute.ZXING_ROUTE)
                        }
                    }
                )
            }
        }

        // 展示弹窗
        if (isShowDialog) {
            LogoutAlderDialog(
                confirmClick = {
                    viewModel.logoutAccount()
                    coroutineScope.launch {
                        NavUtil.doPageNavigationTo(navController, PageRoute.LOGIN_ROUTE, false)
                    }
                }
            )
        }
    }
}




