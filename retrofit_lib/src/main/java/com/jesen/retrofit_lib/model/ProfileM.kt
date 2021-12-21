package com.jesen.retrofit_lib.model

import android.os.Parcelable
import com.jesen.retrofit_lib.com.BaseModel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize


@JsonClass(generateAdapter = true)
@Parcelize
data class ProfileM(
    @Json(name = "code")
    override val code: Int,
    @Json(name = "data")
    override val `data`: DataProfile,
    @Json(name = "msg")
    override val msg: String
) : BaseModel(code, data, msg), Parcelable

@JsonClass(generateAdapter = true)
@Parcelize
data class DataProfile(
    @Json(name = "name")
    val name: String,
    @Json(name = "face")
    val face: String,
    @Json(name = "fans")
    val fans: Int,
    @Json(name = "favorite")
    val favorite: Int,
    @Json(name = "like")
    val like: Int,
    @Json(name = "coin")
    val coin: Int,
    @Json(name = "browsing")
    val browsing: Int,
    @Json(name = "bannerList")
    val bannerList: List<BannerM>?,
    @Json(name = "courseList")
    val courseList: List<Course>?,
    @Json(name = "benefitList")
    val benefitList: List<Benefit>?
) : Parcelable

/*@JsonClass(generateAdapter = true)
@Parcelize
data class BannerM(
    @Json(name = "id")
    val id: String,
    @Json(name = "sticky")
    val sticky: Int,
    @Json(name = "type")
    val type: String,
    @Json(name = "title")
    val title: String,
    @Json(name = "subtitle")
    val subtitle: String,
    @Json(name = "url")
    val url: String,
    @Json(name = "cover")
    val cover: String,
    @Json(name = "createTime")
    val createTime: String
) : Parcelable*/

@JsonClass(generateAdapter = true)
@Parcelize
data class Course(
    @Json(name = "name")
    val name: String,
    @Json(name = "cover")
    val cover: String,
    @Json(name = "url")
    val url: String,
    @Json(name = "group")
    val group: Int
) : Parcelable

@JsonClass(generateAdapter = true)
@Parcelize
data class Benefit(
    @Json(name = "name")
    val name: String,
    @Json(name = "url")
    val url: String
) : Parcelable