package com.jesen.compose_bili.datasource

import com.jesen.common_util_lib.paging.CommonListPagingDataSource
import com.jesen.compose_bili.repository.LazyColumnRepository
import com.jesen.retrofit_lib.api.ParamSort
import com.jesen.retrofit_lib.model.VideoM

class RankingListPagingDataSource(private val sort: ParamSort) :
    CommonListPagingDataSource<VideoM>() {

    override suspend fun provideDataList(params: LoadParams<Int>): List<VideoM> {

        val currentPage = params.key ?: 1
        val pageSize = params.loadSize

        return LazyColumnRepository.getRankingVideoList(
            sort = sort.value,
            pageIndex = currentPage,
            pageSize = pageSize
        )
            .data?.list ?: emptyList()
    }
}