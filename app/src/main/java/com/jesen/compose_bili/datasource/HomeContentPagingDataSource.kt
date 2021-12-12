package com.jesen.compose_bili.datasource

import com.jesen.compose_bili.base.CommonListPagingDataSource
import com.jesen.compose_bili.model.CategoryM
import com.jesen.compose_bili.model.VideoM
import com.jesen.compose_bili.repository.HomeCategoryRepository
import com.jesen.compose_bili.viewmodel.HomeViewModel

/**
 * 首页各栏目的数据请求分页
 * */
class HomeContentPagingDataSource(
    private val categoryM: CategoryM,
    private val viewModel: HomeViewModel,
    private val pageIndex: Int
) : CommonListPagingDataSource<VideoM>() {

    override suspend fun provideDataList(params: LoadParams<Int>): List<VideoM> {
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

        return responseResult.data.videoList
    }
}