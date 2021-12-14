package com.jesen.videodetail_model.model


import android.os.Parcelable
import com.jesen.retrofit_lib.model.VideoM
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class VideoDetailM(
    @Json(name = "code")
    val code: Int,
    @Json(name = "data")
    val `data`: DetailData,
    @Json(name = "msg")
    val msg: String
) : Parcelable


@JsonClass(generateAdapter = true)
@Parcelize
data class DetailData(
    @Json(name = "isFavorite")
    val isFavorite: Boolean,
    @Json(name = "isLike")
    val isLike: Boolean,
    @Json(name = "videoInfo")
    val videoInfo: VideoM,
    @Json(name = "videoList")
    val videoList: List<VideoM>
) : Parcelable
