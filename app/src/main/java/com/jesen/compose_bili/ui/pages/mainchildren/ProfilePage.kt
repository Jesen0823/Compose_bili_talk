package com.jesen.compose_bili.ui.pages.mainchildren

import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import com.google.accompanist.pager.ExperimentalPagerApi
import com.jesen.bilibanner.BannerConfig
import com.jesen.common_util_lib.custonnested.NestedWrapCustomLayout
import com.jesen.compose_bili.MainActivity
import com.jesen.compose_bili.ui.widget.*
import com.jesen.compose_bili.viewmodel.ProfileViewModel
import com.jesen.retrofit_lib.model.DataProfile
import com.jesen.retrofit_lib.response.DataState
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

    Box(modifier = Modifier.fillMaxSize()) {

        // 收集流数据
        val profilePageData by viewModel.profileDataState.collectAsState()

        // 判断视频是否加载了
        fun isProfileLoaded() = profilePageData is DataState.Success

        // 加载视频信息
        LaunchedEffect(Unit) {
            if (!isProfileLoaded()) {
                viewModel.loadProfileInfo()
            }
        }

        // 观察流数据状态更新UI
        MaterialFadeThrough(
            targetState = profilePageData
        ) { dataStoreData ->
            when (dataStoreData) {
                is DataState.Empty,
                is DataState.Loading -> {
                    ProfileLoading()
                }
                is DataState.Success -> {
                    // 展示详情内容
                    ProfileContentScreen(viewModel, dataStoreData.read().data)
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
@ExperimentalCoilApi
@ExperimentalPagerApi
@ExperimentalFoundationApi
@Composable
fun ProfileContentScreen(viewModel: ProfileViewModel, profileData: DataProfile) {
    NestedWrapCustomLayout(
        columnTop = 200.dp,
        navigationIconSize = 80.dp,
        toolBarHeight = 50.dp,
        scrollableAppBarHeight = 200.dp,
        columnState = rememberLazyListState(),
        scrollableAppBarBgColor = Color.LightGray,
        toolBar = { ProfileToolBar(profileData = profileData) },
        navigationIcon = { UserAdvertImg(advert = profileData.face) }, //默认为返回图标
        extendUsrInfo = { UserNameUI(profileData = profileData) },
        headerTop = { HeaderTop() },
        backSlideProgress = { progress ->

        }
    ) {
        /** 广告banner */
        /** 广告banner */
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
                }
            )
        }

        /** 广告课程 */

        /** 广告课程 */

        stickyHeader {
            ColumnStickHeader(title = "你的广告", subTitle = "这些都是flutter课程广告")
        }
        items(50) {
            Text(text = "HHHHHHHHHHHHHHHHHHHHHHH")
        }
        /* profileData.courseList?.let {
             item {
                 LazyVerticalGrid(
                     modifier=Modifier.wrapContentHeight().fillMaxWidth(),
                     verticalArrangement = Arrangement.spacedBy(4.dp),
                     horizontalArrangement = Arrangement.spacedBy(4.dp),
                     cells = GridCells.Fixed(2),
                     state = rememberLazyListState(),
                     contentPadding = PaddingValues(8.dp),
                     content = {
                         items(count = it.size) { index -> CourseItemView(it[index], onclick = {}) }
                     }
                 )
             }
         }*/


        /**
         * 增值服务
         * */


        /**
         * 增值服务
         * */
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
                        items(count = it.size) { index -> BenefitItemView(it[index], onclick = {}) }
                    }
                )

            }

        }
    }
}




