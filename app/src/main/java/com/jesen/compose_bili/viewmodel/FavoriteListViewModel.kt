package com.jesen.compose_bili.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.jesen.compose_bili.datasource.FavoriteListPagingDataSource

class FavoriteListViewModel : ViewModel() {

    //项目列表数据
    val favoriteListData = Pager(
        config = PagingConfig(
            pageSize = 5,
            initialLoadSize = 10, // 第一次加载数量
            prefetchDistance = 1,
        )
    ) {
        FavoriteListPagingDataSource()
    }.flow.cachedIn(viewModelScope)


}