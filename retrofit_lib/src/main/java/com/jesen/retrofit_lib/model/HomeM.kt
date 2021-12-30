package com.jesen.retrofit_lib.model

import android.os.Parcelable
import com.jesen.retrofit_lib.com.fromJson
import com.jesen.retrofit_lib.com.toJson
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class HomeM(
    @Json(name = "code")
    val code: Int,
    @Json(name = "data")
    val `data`: HomeData,
    @Json(name = "msg")
    val msg: String
) : Parcelable


@JsonClass(generateAdapter = true)
@Parcelize
data class VideoM(
    @Json(name = "coin")
    val coin: Int = 0,
    @Json(name = "cover")
    val cover: String = "",
    @Json(name = "createTime")
    val createTime: String = "",
    @Json(name = "desc")
    val desc: String = "",
    @Json(name = "duration")
    val duration: Int = 0,
    @Json(name = "favorite")
    val favorite: Int = 0,
    @Json(name = "id")
    val id: String = "",
    @Json(name = "like")
    val like: Int = 0,
    @Json(name = "owner")
    val owner: OwnerM? = null,
    @Json(name = "pubdate")
    val pubdate: Int = 0,
    @Json(name = "reply")
    val reply: Int = 0,
    @Json(name = "share")
    val share: Int = 0,
    @Json(name = "size")
    val size: Int = 0,
    @Json(name = "title")
    val title: String = "",
    @Json(name = "tname")
    val tname: String = "",
    @Json(name = "url")
    val url: String = "",
    @Json(name = "vid")
    val vid: String = "",
    @Json(name = "view")
    val view: Int = 0
) : Parcelable

@JsonClass(generateAdapter = true)
@Parcelize
data class BannerM(
    @Json(name = "cover")
    val cover: String,
    @Json(name = "createTime")
    val createTime: String,
    @Json(name = "id")
    val id: String,
    @Json(name = "sticky")
    val sticky: Int,
    @Json(name = "subtitle")
    val subtitle: String,
    @Json(name = "title")
    val title: String,
    @Json(name = "type")
    val type: String,
    @Json(name = "url")
    val url: String
) : Parcelable

@JsonClass(generateAdapter = true)
@Parcelize
data class CategoryM(
    @Json(name = "count")
    val count: Int,
    @Json(name = "name")
    val name: String
) : Parcelable

@JsonClass(generateAdapter = true)
@Parcelize
data class OwnerM(
    @Json(name = "face")
    val face: String,
    @Json(name = "fans")
    val fans: Int,
    @Json(name = "name")
    val name: String
) : Parcelable

@JsonClass(generateAdapter = true)
@Parcelize
data class HomeData(
    @Json(name = "bannerList")
    val bannerList: List<BannerM>? = null,
    @Json(name = "categoryList")
    val categoryList: List<CategoryM>? = null,
    @Json(name = "videoList")
    val videoList: List<VideoM>
) : Parcelable

fun videoJs2Model(js: String) = js.fromJson<VideoM>()
fun videoModel2Js(video: VideoM) = video.toJson()