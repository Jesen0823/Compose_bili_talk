package com.jesen.compose_bili.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.jesen.compose_bili.model.CategoryM
import com.jesen.compose_bili.model.VideoM
import com.jesen.compose_bili.repository.HomeCategoryRepository
import com.jesen.compose_bili.utils.oLog
import com.jesen.compose_bili.viewmodel.HomeViewModel

class HomeVideoListDataSource(
    private val categoryM: CategoryM,
    private val viewModel: HomeViewModel,
    private val pageIndex: Int
) : PagingSource<Int, VideoM>() {

    override fun getRefreshKey(state: PagingState<Int, VideoM>): Int? = null

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, VideoM> {
        return try {
            val currentPage = params.key ?: 1
            val pageSize = params.loadSize

            // 获取数据
            val responseResult =
                HomeCategoryRepository.requestHome(
                    category = categoryM.name,
                    pageIndex = currentPage,
                    pageSize = pageSize
                )

            // 保存栏目列表
            if (responseResult.code == 0 && pageIndex == 0) {
                if (responseResult.data.categoryList?.isNotEmpty() == true && viewModel.categoryList.size == 1) {
                    viewModel.categoryList.clear()
                    viewModel.categoryList.addAll(responseResult.data.categoryList)
                }

                responseResult.data.bannerList?.let {
                    viewModel.bannerList = it
                }
            }

            val videoList = responseResult.data.videoList
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