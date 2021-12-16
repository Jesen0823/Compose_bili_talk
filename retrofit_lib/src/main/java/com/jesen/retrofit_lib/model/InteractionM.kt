package com.jesen.retrofit_lib.model

import android.os.Parcelable
import com.jesen.retrofit_lib.com.BaseModel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

/**
 * 互动交互，只返回code,msg
 * */
@JsonClass(generateAdapter = true)
@Parcelize
data class InteractionM(
    @Json(name = "code")
    override val code: Int,
    @Json(name = "data")
    override val `data`: Boolean? = code == 0,
    @Json(name = "msg")
    override val msg: String
) : BaseModel(code, data, msg), Parcelable