package com.jesen.compose_bili.datasource

import com.jesen.compose_bili.model.VideoM
import com.jesen.compose_bili.network.api.ParamSort
import com.jesen.compose_bili.repository.LazyColumnRepository

class RankingListDataSource(private val sort: ParamSort) : ColumnListDataSource<VideoM>() {

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