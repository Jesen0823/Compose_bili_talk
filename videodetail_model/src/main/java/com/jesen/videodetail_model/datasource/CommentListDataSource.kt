package com.jesen.videodetail_model.datasource

import com.jesen.common_util_lib.paging.CommonListPagingDataSource
import com.jesen.retrofit_lib.model.Comment
import com.jesen.videodetail_model.repository.VideoDetailRepository


/**
 * 评论列表分页
 * 虽然 继承了 [CommonListPagingDataSource]
 * 但是此处是以上一页最后一项分页的，请求入参是不一样的实现
 * */
class CommentListDataSource : CommonListPagingDataSource<Comment>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Comment> {

        return try {
            val currentLastItem = params.key ?: 0
            val pageSize = params.loadSize

            // 上一页key
            val preKey = null

            val response =
                VideoDetailRepository.getCommitList(pageCount = pageSize, lastid = currentLastItem)

            val responseList = response.data.data

            var nextKey: Int? = null
            if (!responseList.isNullOrEmpty()) {
                // 下一页key
                nextKey = responseList.last().id
            }

            LoadResult.Page(
                data = responseList!!,
                prevKey = preKey,
                nextKey = nextKey
            )
        } catch (e: Exception) {
            e.printStackTrace()
            LoadResult.Error(e)
        }

    }

    override suspend fun provideDataList(params: LoadParams<Int>): List<Comment> {
        return emptyList()
    }

}