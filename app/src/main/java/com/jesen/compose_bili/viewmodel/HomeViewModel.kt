package com.jesen.compose_bili.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.jesen.compose_bili.model.BannerM
import com.jesen.compose_bili.model.CategoryM
import com.jesen.compose_bili.model.VideoM
import com.jesen.compose_bili.repository.HomeCategoryRepository
import com.jesen.compose_bili.repository.HomeVideoListDataSource
import kotlinx.coroutines.flow.Flow

class HomeViewModel : ViewModel() {

    var categoryList = mutableListOf(CategoryM(count = 1, name = "推荐"))

    var bannerList: List<BannerM>? = null

    fun getVideoListIndexOf(index: Int): Flow<PagingData<VideoM>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5,
                initialLoadSize = 10, // 第一次加载数量
                prefetchDistance = 1,
            )
        ) {

            HomeVideoListDataSource(HomeCategoryRepository, viewModel = this, index)
        }.flow.cachedIn(viewModelScope)
    }

}