package com.jesen.compose_bili.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.jesen.compose_bili.model.VideoM
import com.jesen.compose_bili.utils.oLog
import com.jesen.compose_bili.viewmodel.HomeViewModel

class HomeVideoListDataSource(
    private val repository: HomeCategoryRepository,
    private val viewModel: HomeViewModel,
    private val pageIndex: Int
) : PagingSource<Int, VideoM>() {

    override fun getRefreshKey(state: PagingState<Int, VideoM>): Int? = null

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, VideoM> {
        return try {
            val currentPage = params.key ?: 1
            val pageSize = params.loadSize
            val categoryList = viewModel.categoryList

            val categoryName = if (categoryList.size == 1) "推荐" else categoryList[pageIndex].name

            val responseResult =
                repository.requestHome(category = categoryName, currentPage, pageSize)

            // 保存栏目列表
            if (responseResult.code == 0 && pageIndex == 0) {
                responseResult.data.categoryList?.let {
                    viewModel.categoryList.apply {
                        clear()
                        addAll(it)
                    }
                }

                responseResult.data.bannerList?.let {
                    viewModel.bannerList = it
                }
            }

            val videoList = responseResult?.data.videoList
            // 上一页页码
            val preKey = if (currentPage == 1) null else currentPage.minus(1)
            // 下一页页码
            var nextKey: Int? = currentPage.plus(1)
            oLog("load currentPage: $currentPage")
            oLog("load preKey: $preKey")
            oLog("load nextKey: $nextKey")
            if (videoList.isEmpty()) {
                nextKey = null
            }

            LoadResult.Page(
                data = videoList,
                prevKey = preKey,
                nextKey = nextKey
            )

        } catch (e: Exception) {
            e.printStackTrace()
            LoadResult.Error(e)
        }
    }
}