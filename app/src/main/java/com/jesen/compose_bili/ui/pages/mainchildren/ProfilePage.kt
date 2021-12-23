package com.jesen.compose_bili.ui.pages.mainchildren

import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import coil.annotation.ExperimentalCoilApi
import com.google.accompanist.pager.ExperimentalPagerApi
import com.jesen.bilibanner.BannerConfig
import com.jesen.common_util_lib.custonnested.NestedWrapCustomLayout
import com.jesen.compose_bili.MainActivity
import com.jesen.compose_bili.navigation.PageRoute
import com.jesen.compose_bili.navigation.doPageNavigationTo
import com.jesen.compose_bili.ui.widget.*
import com.jesen.compose_bili.viewmodel.ProfileViewModel
import com.jesen.retrofit_lib.model.DataProfile
import com.jesen.retrofit_lib.response.DataState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import soup.compose.material.motion.MaterialFadeThrough


/**
 * 个人中心
 */
@ExperimentalFoundationApi
@ExperimentalMaterialApi
@ExperimentalAnimationApi
@ExperimentalPagerApi
@Composable
fun ProfilePage(activity: MainActivity) {

    val viewModel by activity.viewModels<ProfileViewModel>()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(key1 = viewModel.profileDataState) {
        activity.lifecycleScope.launch {
            viewModel.loadProfileInfo()
        }
    }


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
                    ProfileContentScreen(coroutineScope, viewModel, dataStoreData.value.read().data)
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
@ExperimentalAnimationApi
@ExperimentalMaterialApi
@ExperimentalCoilApi
@ExperimentalPagerApi
@ExperimentalFoundationApi
@Composable
fun ProfileContentScreen(
    coroutineScope: CoroutineScope,
    viewModel: ProfileViewModel,
    profileData: DataProfile
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
    ) {

        NestedWrapCustomLayout(
            columnTop = 202.dp,
            navigationIconSize = 80.dp,
            toolBarHeight = 56.dp,
            scrollableAppBarHeight = 202.dp,
            columnState = rememberLazyListState(),
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
                    modifier = Modifier.fillMaxWidth(),
                    items = viewModel.bannerDataList,
                    config = BannerConfig(
                        indicatorColor = Color.Gray.copy(0.6f),
                        selectedColor = Color.White.copy(0.8f),
                        intervalTime = 2500
                    ),

                    itemOnClick = { banner ->
                        // 点击banner
                        coroutineScope.launch {
                            doPageNavigationTo(PageRoute.WEBVIEW_ROUTE)
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
            profileData.benefitList?.let {
                item {
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceAround,
                        state = rememberLazyListState(),
                        contentPadding = PaddingValues(vertical = 8.dp, horizontal = 2.dp),
                        content = {
                            items(count = it.size) { index ->
                                BenefitItemView(
                                    it[index],
                                    onclick = {})
                            }
                        }
                    )

                }
            }
        }
    }
}




