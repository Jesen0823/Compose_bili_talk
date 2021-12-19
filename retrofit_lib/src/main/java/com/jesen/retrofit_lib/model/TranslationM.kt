package com.jesen.retrofit_lib.model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class TranslationM(
    @Json(name = "data")
    val `data`: TransData?,
    @Json(name = "result")
    val result: Status?
) : Parcelable

@JsonClass(generateAdapter = true)
@Parcelize
data class TransData(
    @Json(name = "entries")
    val entries: List<TransEntry>?,
    @Json(name = "language")
    val language: String?,
    @Json(name = "query")
    val query: String?,
    @Json(name = "type")
    val type: String?
) : Parcelable

@JsonClass(generateAdapter = true)
@Parcelize
data class Status(
    @Json(name = "code")
    val code: Int,
    @Json(name = "msg")
    val msg: String
) : Parcelable

@JsonClass(generateAdapter = true)
@Parcelize
data class TransEntry(
    @Json(name = "entry")
    val entry: String?,
    @Json(name = "explain")
    val explain: String?
) : Parcelable