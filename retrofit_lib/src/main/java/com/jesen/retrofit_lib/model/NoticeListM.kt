package com.jesen.retrofit_lib.model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize


@JsonClass(generateAdapter = true)
@Parcelize
data class NoticeListM(
    @Json(name = "code")
    val code: Int,
    @Json(name = "data")
    val `data`: Data,
    @Json(name = "msg")
    val msg: String
) : Parcelable

@JsonClass(generateAdapter = true)
@Parcelize
data class Data(
    @Json(name = "total")
    val total: Int,
    @Json(name = "list")
    val list: List<Notice>?
) : Parcelable

@JsonClass(generateAdapter = true)
@Parcelize
data class Notice(
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
    val cover: String?,
    @Json(name = "createTime")
    val createTime: String
) : Parcelable