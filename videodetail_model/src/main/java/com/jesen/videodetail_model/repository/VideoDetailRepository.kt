package com.jesen.videodetail_model.repository

import com.jesen.retrofit_lib.RetrofitClient
import com.jesen.retrofit_lib.api.CommitApi
import com.jesen.retrofit_lib.api.FavoriteServiceApi
import com.jesen.retrofit_lib.api.LikeServiceApi
import com.jesen.retrofit_lib.api.VideoDetailApi
import com.jesen.retrofit_lib.model.InteractionM
import kotlinx.coroutines.delay

object VideoDetailRepository {

    suspend fun getVideDetailData(vid: String) =
        RetrofitClient.createApi(VideoDetailApi::class.java)
            .requestVideoDetail(videoId = vid)

    suspend fun followUpAuthor(follow: Boolean): InteractionM {
        delay(1500)
        return InteractionM(code = 0, data = !follow, msg = if (follow) "成功取消关注" else "关注成功")
    }

    suspend fun likeTheVideo(vid: String, isLike: Boolean) =
        RetrofitClient.createApi(LikeServiceApi::class.java)
            .let {
                if (isLike) it.requestLikeVideo(vid) else it.requestCancelLikeVideo(vid)
            }

    suspend fun favoriteTheVideo(vid: String, isFavorite: Boolean) =
        RetrofitClient.createApi(FavoriteServiceApi::class.java)
            .let {
                if (isFavorite) it.requestFavoriteVideo(vid) else it.requestCancelFavoriteVideo(vid)
            }


    suspend fun getCommitList(pageCount: Int, lastid: Int) =
        RetrofitClient.createApi(CommitApi::class.java)
            .requestCommentList(pageCount = pageCount, id = lastid)

    suspend fun addComment(content: String) =
        RetrofitClient.createApi(CommitApi::class.java)
            .requestAddComment(commentText = content)

    suspend fun likeComment(commentId: String) =
        RetrofitClient.createApi(CommitApi::class.java)
            .requestLikeComment(commentId = commentId)


}