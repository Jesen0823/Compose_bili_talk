package com.jesen.compose_bili.datasource

import com.jesen.common_util_lib.paging.CommonListPagingDataSource
import com.jesen.compose_bili.repository.NoticeListRepository
import com.jesen.retrofit_lib.model.Notice

/**
 * 通知列表分页处理
 * */
class NoticeListPagingDataSource : CommonListPagingDataSource<Notice>() {

    override suspend fun provideDataList(params: LoadParams<Int>): List<Notice> {
        val currentPage = params.key ?: 1
        val pageSize = params.loadSize

        return NoticeListRepository.requestNotices(
            pageIndex = currentPage,
            pageSize = pageSize
        ).data?.list ?: emptyList()
    }
}