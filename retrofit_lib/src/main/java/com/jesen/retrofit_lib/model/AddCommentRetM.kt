package com.jesen.retrofit_lib.model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize


@JsonClass(generateAdapter = true)
@Parcelize
data class AddComRetM(
    @Json(name = "status")
    val status: Int,
    @Json(name = "message")
    val message: String,
    @Json(name = "data")
    val `data`: CommentResult
) : Parcelable

@JsonClass(generateAdapter = true)
@Parcelize
data class CommentResult(
    @Json(name = "data")
    var `data`: Comment?
) : Parcelable

@JsonClass(generateAdapter = true)
@Parcelize
data class LikeCommentM(
    @Json(name = "status")
    val status: Int,
    @Json(name = "message")
    val message: String,
    @Json(name = "data")
    val `data`: LikeCmtRet
) : Parcelable

@JsonClass(generateAdapter = true)
@Parcelize
data class LikeCmtRet(
    @Json(name = "data")
    val `data`: Like
) : Parcelable

@JsonClass(generateAdapter = true)
@Parcelize
data class Like(
    @Json(name = "hasLiked")
    val hasLiked: Boolean
) : Parcelable