package com.jesen.compose_bili.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.jesen.compose_bili.model.VideoM
import com.jesen.compose_bili.network.api.ParamSort
import com.jesen.compose_bili.repository.LazyColumnRepository

class VideoColumnListDataSource(
    private val sort: ParamSort
) :
    PagingSource<Int, VideoM>() {

    override fun getRefreshKey(state: PagingState<Int, VideoM>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, VideoM> {
        return try {
            val currentPage = params.key ?: 1
            val pageSize = params.loadSize

            val responseList =
                LazyColumnRepository.getRankingVideoList(
                    sort = sort.value,
                    pageIndex = currentPage,
                    pageSize = pageSize
                )
                    .data?.list ?: emptyList<VideoM>()
            // 上一页页码
            val preKey = if (currentPage == 1) null else currentPage.minus(1)
            // 下一页页码
            var nextKey: Int? = currentPage.plus(1)

            if (responseList.isEmpty()) {
                nextKey = null
            }

            LoadResult.Page(
                data = responseList,
                prevKey = preKey,
                nextKey = nextKey
            )
        } catch (e: Exception) {
            e.printStackTrace()
            LoadResult.Error(e)
        }
    }
}