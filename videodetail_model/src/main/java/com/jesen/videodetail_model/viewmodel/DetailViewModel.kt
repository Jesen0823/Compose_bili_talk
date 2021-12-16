package com.jesen.videodetail_model.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.jesen.common_util_lib.utils.oLog
import com.jesen.retrofit_lib.com.fromJson
import com.jesen.retrofit_lib.model.*
import com.jesen.retrofit_lib.response.DataState
import com.jesen.retrofit_lib.response.ModelMapper
import com.jesen.retrofit_lib.response.Response
import com.jesen.videodetail_model.datasource.CommentListDataSource
import com.jesen.videodetail_model.repository.VideoDetailRepository
import com.jesen.videodetail_model.util.videoDataDemoJson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class DetailViewModel : ViewModel() {

    // 详情页数据状态
    val videoDetailState = MutableStateFlow<DataState<VideoDetailM>>(DataState.Empty)

    // 关注/取关作者状态，没有对应API，来个假的
    private val upFollowState = MutableStateFlow<DataState<InteractionM>>(DataState.Empty)

    // 点赞/取消赞状态
    private val videoLikeState = MutableStateFlow<DataState<InteractionM>>(DataState.Empty)

    // 收藏/取消收藏状态
    private val videoFavoriteState = MutableStateFlow<DataState<InteractionM>>(DataState.Empty)

    var testVideoM: VideoM = videoDataDemoJson.fromJson<VideoM>()!!

    fun loadVideoInfo2(vid: String) = viewModelScope.launch {
        videoDetailState.value = DataState.Loading
        flow {
            val result = VideoDetailRepository.getVideDetailData(vid)

            oLog(" login result: ${result.msg}")
            emit(result)

        }.flowOn(Dispatchers.IO)
            .map { model ->
                ModelMapper<VideoDetailM>().map(model)
            }
            .collect { response ->
                if (response.isSuccess()) {
                    videoDetailState.value = DataState.Success(response.read())

                    // insert history
                } else {
                    videoDetailState.value =
                        DataState.Error(response.errorMessage(), response.code())
                }
            }
    }

    fun requestFollow(isFollow: Boolean, result: (response: Response<InteractionM>?) -> Unit) =
        viewModelScope.launch {
            upFollowState.value = DataState.Loading
            var response: Response<InteractionM>? = null

            flow {
                val result = VideoDetailRepository.followUpAuthor(isFollow)
                oLog(" follow result: ${result.msg}")
                emit(result)

            }.flowOn(Dispatchers.IO)
                .map { model ->
                    ModelMapper<InteractionM>().map(model)
                }
                .collect { res ->
                    if (res.isSuccess()) {
                        upFollowState.value = DataState.Success(res.read())

                    } else {
                        upFollowState.value =
                            DataState.Error(res.errorMessage(), res.code())
                    }
                    response = res
                }
            result(response)
        }

    fun requestLike(
        isLike: Boolean,
        videoId: String,
        result: (response: Response<InteractionM>?) -> Unit
    ) =
        viewModelScope.launch {
            videoLikeState.value = DataState.Loading
            var response: Response<InteractionM>? = null

            flow {
                val result = VideoDetailRepository.likeTheVideo(videoId, isLike)
                oLog(" like/unLike result: ${result.msg},data:${result.data}")
                emit(result)

            }.flowOn(Dispatchers.IO)
                .map { model ->
                    ModelMapper<InteractionM>().map(model)
                }
                .collect { res ->
                    if (res.isSuccess()) {
                        videoLikeState.value = DataState.Success(res.read())

                    } else {
                        videoLikeState.value =
                            DataState.Error(res.errorMessage(), res.code())
                    }
                    response = res
                }
            result(response)
        }

    fun requestFavorite(
        isFavorite: Boolean,
        videoId: String,
        result: (response: Response<InteractionM>?) -> Unit
    ) =
        viewModelScope.launch {
            videoFavoriteState.value = DataState.Loading
            var response: Response<InteractionM>? = null

            flow {
                val result = VideoDetailRepository.favoriteTheVideo(videoId, isFavorite)
                oLog(" favorite/unFavorite result: ${result.msg},data:${result.data}")
                emit(result)

            }.flowOn(Dispatchers.IO)
                .map { model ->
                    ModelMapper<InteractionM>().map(model)
                }
                .collect { res ->
                    if (res.isSuccess()) {
                        videoFavoriteState.value = DataState.Success(res.read())
                    } else {
                        videoFavoriteState.value =
                            DataState.Error(res.errorMessage(), res.code())
                    }
                    response = res
                }
            result(response)
        }


    //评论列表数据
    val getCommentListData by lazy {
        Pager(
            config = PagingConfig(
                pageSize = 5,
                initialLoadSize = 10, // 第一次加载数量
                prefetchDistance = 1,
            )
        ) {
            CommentListDataSource()
        }.flow.cachedIn(viewModelScope)
    }

    fun addNewComment(content: String, result: (response: AddComRetM?) -> Unit) =
        viewModelScope.launch {
            var response: AddComRetM? = null

            flow {
                val ret = VideoDetailRepository.addComment(content)
                emit(ret)
            }.flowOn(Dispatchers.IO)
                .collect { res ->
                    response = res
                }
            result(response)
        }

    fun likeComment(commentId: String, result: (response: LikeCommentM?) -> Unit) =
        viewModelScope.launch {
            var response: LikeCommentM? = null

            flow {
                val ret = VideoDetailRepository.likeComment(commentId)
                emit(ret)
            }.flowOn(Dispatchers.IO)
                .collect { res ->
                    response = res
                }
            result(response)
        }
}