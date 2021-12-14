package com.jesen.retrofit_lib.model


import android.os.Parcelable
import com.jesen.retrofit_lib.com.BaseModel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class VideoDetailM(
    @Json(name = "code")
    override val code: Int,
    @Json(name = "data")
    override val `data`: VideoDetailData,
    @Json(name = "msg")
    override val msg: String
) : BaseModel(code, data, msg), Parcelable


@JsonClass(generateAdapter = true)
@Parcelize
data class VideoDetailData(
    @Json(name = "isFavorite")
    val isFavorite: Boolean,
    @Json(name = "isLike")
    val isLike: Boolean,
    @Json(name = "videoInfo")
    val videoInfo: VideoM,
    @Json(name = "videoList")
    val videoList: List<VideoM>
) : Parcelable
