package com.jesen.retrofit_lib.model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize


@JsonClass(generateAdapter = true)
@Parcelize
data class SaCommentListM(
    @Json(name = "status")
    val status: Int,
    @Json(name = "message")
    val message: String,
    @Json(name = "data")
    val `data`: ListData
) : Parcelable

@JsonClass(generateAdapter = true)
@Parcelize
data class ListData(
    @Json(name = "data")
    val `data`: List<Comment>?
) : Parcelable

@JsonClass(generateAdapter = true)
@Parcelize
data class Comment(
    @Json(name = "id")
    val id: Int,
    @Json(name = "itemId")
    val itemId: Long,
    @Json(name = "commentId")
    val commentId: Long,
    @Json(name = "userId")
    val userId: Int,
    @Json(name = "commentType")
    val commentType: Int,
    @Json(name = "createTime")
    val createTime: Long,
    @Json(name = "commentCount")
    val commentCount: Int,
    @Json(name = "likeCount")
    val likeCount: Int,
    @Json(name = "commentText")
    val commentText: String,
    @Json(name = "imageUrl")
    val imageUrl: String,
    @Json(name = "videoUrl")
    val videoUrl: String,
    @Json(name = "width")
    val width: Int,
    @Json(name = "height")
    val height: Int,
    @Json(name = "hasLiked")
    val hasLiked: Boolean,
    @Json(name = "author")
    val author: Author,
    @Json(name = "ugc")
    val ugc: Ugc?
) : Parcelable

@JsonClass(generateAdapter = true)
@Parcelize
data class Author(
    @Json(name = "id")
    val id: Int,
    @Json(name = "userId")
    val userId: Int,
    @Json(name = "name")
    val name: String,
    @Json(name = "avatar")
    val avatar: String,
    @Json(name = "description")
    val description: String?,
    @Json(name = "likeCount")
    val likeCount: Int,
    @Json(name = "topCommentCount")
    val topCommentCount: Int,
    @Json(name = "followCount")
    val followCount: Int,
    @Json(name = "followerCount")
    val followerCount: Int,
    @Json(name = "qqOpenId")
    val qqOpenId: String,
    @Json(name = "expires_time")
    val expiresTime: Long,
    @Json(name = "score")
    val score: Int,
    @Json(name = "historyCount")
    val historyCount: Int,
    @Json(name = "commentCount")
    val commentCount: Int,
    @Json(name = "favoriteCount")
    val favoriteCount: Int,
    @Json(name = "feedCount")
    val feedCount: Int,
    @Json(name = "hasFollow")
    val hasFollow: Boolean
) : Parcelable

@JsonClass(generateAdapter = true)
@Parcelize
data class Ugc(
    @Json(name = "likeCount")
    val likeCount: Int,
    @Json(name = "shareCount")
    val shareCount: Int,
    @Json(name = "commentCount")
    val commentCount: Int,
    @Json(name = "hasFavorite")
    val hasFavorite: Boolean,
    @Json(name = "hasLiked")
    val hasLiked: Boolean,
    @Json(name = "hasdiss")
    val hasdiss: Boolean,
    @Json(name = "hasDissed")
    val hasDissed: Boolean
) : Parcelable