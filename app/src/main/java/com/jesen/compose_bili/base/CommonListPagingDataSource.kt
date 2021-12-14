package com.jesen.compose_bili.base

import androidx.paging.PagingSource
import androidx.paging.PagingState

/**
 * 给简单列表封装一个公共DataSource
 * */
abstract class CommonListPagingDataSource<T : Any>() :
    PagingSource<Int, T>() {

    override fun getRefreshKey(state: PagingState<Int, T>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
        return try {
            val currentPage = params.key ?: 1
            val responseList = provideDataList(params)

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

    // 处理结果，返回实体列表
    abstract suspend fun provideDataList(params: LoadParams<Int>): List<T>
}