package com.jesen.compose_bili.viewmodel

import androidx.compose.foundation.lazy.LazyListState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.jesen.compose_bili.datasource.RankingListPagingDataSource
import com.jesen.retrofit_lib.api.ParamSort


class RankingViewModel() : ViewModel() {

    // 已选列表项记忆
    var selectedIndex = -1

    //项目页面列表状态
    var lazyColumnStateList: MutableList<LazyListState> = mutableListOf()

    //项目列表数据
    val rankListData = ParamSort.values().map { sort ->
        sort to Pager(
            config = PagingConfig(
                pageSize = 5,
                initialLoadSize = 10, // 第一次加载数量
                prefetchDistance = 1,
            )
        ) {
            RankingListPagingDataSource(sort)
        }.flow.cachedIn(viewModelScope)
    }.toList()
}

