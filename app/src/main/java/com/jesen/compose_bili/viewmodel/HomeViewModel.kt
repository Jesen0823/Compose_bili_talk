package com.jesen.compose_bili.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.jesen.bilibanner.bean.BannerData
import com.jesen.compose_bili.datasource.HomeContentPagingDataSource
import com.jesen.compose_bili.model.CategoryM
import com.jesen.compose_bili.model.VideoM
import com.jesen.compose_bili.utils.mapper.EntityBannerMapper
import kotlinx.coroutines.flow.Flow

class HomeViewModel : ViewModel() {

    var categoryList = mutableStateListOf(CategoryM(count = 1, name = "推荐"))

    var selectedIndex = 0

    var bannerDataList = mutableStateListOf<BannerData>(BannerData())

    var allCategoryVideoMap: MutableMap<CategoryM, Pair<CategoryM, Flow<PagingData<VideoM>>>>? =
        mutableMapOf()

    fun getVideoPagingDataList(category: CategoryM): Pair<CategoryM, Flow<PagingData<VideoM>>> =
        Pair(
            category, Pager(
                config = PagingConfig(
                    pageSize = 5,
                    initialLoadSize = 10, // 第一次加载数量
                    prefetchDistance = 1,
                )
            ) {

                HomeContentPagingDataSource(category, this, selectedIndex, EntityBannerMapper())
            }.flow.cachedIn(viewModelScope)
        ).also {
            allCategoryVideoMap?.put(category, it)
        }
}