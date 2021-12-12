package com.jesen.compose_bili.datasource

import com.jesen.compose_bili.base.CommonListPagingDataSource
import com.jesen.compose_bili.model.VideoM
import com.jesen.compose_bili.repository.LazyColumnRepository

/**
 * 收藏列表分页处理
 * */
class FavoriteListPagingDataSource : CommonListPagingDataSource<VideoM>() {

    override suspend fun provideDataList(params: LoadParams<Int>): List<VideoM> {
        val currentPage = params.key ?: 1
        val pageSize = params.loadSize

        return LazyColumnRepository.getFavoriteList(
            pageIndex = currentPage,
            pageSize = pageSize
        ).data?.list ?: emptyList()
    }
}