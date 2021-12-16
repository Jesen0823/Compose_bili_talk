package com.jesen.retrofit_lib.api

import com.jesen.retrofit_lib.model.AddComRetM
import com.jesen.retrofit_lib.model.LikeCommentM
import com.jesen.retrofit_lib.model.SaCommentListM
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * 评论接口
 * 第三方接口：【与视频无关，仅仅为了效果展示】
 *
 * */
interface CommitApi {
    /**
     * 评论列表
     * http://123.56.232.18:8080/serverdemo/comment/queryFeedComments?itemId=1615197740457&pageCount=12&id=0&userId=1625048974
     * @param id  上一页数据最后一条的id,如果是第一页请求 则为0
     */

    @GET("http://123.56.232.18:8080/serverdemo/comment/queryFeedComments")
    suspend fun requestCommentList(
        @Query("itemId") itemId: String = "1615197740457",
        @Query("pageCount") pageCount: Int = 10,
        @Query("id") id: Int = 0,
        @Query("userId") userId: String = "1625048974",
    ): SaCommentListM

    /**
     * 发布评论
     * --> POST http://123.56.232.18:8080/serverdemo/comment/addComment
     * itemId=1615197740457&width=0&userId=1625048974&commentText=%E5%BE%97%E5%BE%97%E5%BE%97&height=0
     */
    @POST("http://123.56.232.18:8080/serverdemo/comment/addComment")
    suspend fun requestAddComment(
        @Query("itemId") itemId: String = "1615197740457",
        @Query("width") width: Int = 0,
        @Query("height") height: Int = 0,
        @Query("userId") id: String = "1625048974",
        @Query("commentText") commentText: String
    ): AddComRetM

    /**
     * 点赞评论
     * --> --> GET http://123.56.232.18:8080/serverdemo/ugc/toggleCommentLike?commentId=1639643562119000&userId=1625048974
     */
    @GET("http://123.56.232.18:8080/serverdemo/ugc/toggleCommentLike")
    suspend fun requestLikeComment(
        @Query("commentId") commentId: String,
        @Query("userId") id: String = "1625048974",
    ): LikeCommentM
}